package bronuh.shit.metrics.tools;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;

@Mod.EventBusSubscriber(Side.SERVER)
public class Scheduler {
    private static long ticks = 0;
    private static HashMap<Long,Task> tasks = new HashMap<>();
    private static long lastTaskId = 0;

    @SubscribeEvent
    public static void onServerTick(TickEvent.WorldTickEvent e) {

        if(e.phase == TickEvent.Phase.END)
            return;

        int runned = 0;
        for(Task task : tasks.values()){
                boolean succ = task.tryRun();
                if(succ){
                    runned++;
                }
        }
        ticks++;
    }

    public static long scheduleSyncRepeatingTask(Runnable task, int ticksDelay, int ticksInterval){
        lastTaskId++;
        System.out.println("Scheduled task: ["+lastTaskId+"]");
        tasks.put(lastTaskId, new Task(lastTaskId, task, ticksDelay, Math.max(1,ticksInterval)));
        return lastTaskId;
    }

    public static long scheduleSyncRepeatingTask(Runnable task, int ticksDelay, int ticksInterval, String help){
        lastTaskId++;
        System.out.println("Scheduled task: ["+lastTaskId+"] ("+help+")");
        tasks.put(lastTaskId, new Task(lastTaskId, task, ticksDelay, Math.max(1,ticksInterval),help));
        return lastTaskId;
    }


    public static void cancelTask(long taskId){
        try{
            tasks.remove(taskId);
        }catch(Exception e){
            System.out.println("Ass fucked: "+e.getMessage());
        }
    }



    private static class Task{
        long id;
        long startAt;
        long interval;
        long timesLeft = -1;
        String help;
        Runnable task;

        public Task(long id, Runnable task, int ticksDelay, int ticksInterval){
            this.task = task;
            startAt = ticks + ticksDelay;
            interval = ticksInterval;
            this.id = id;
        }

        public Task(long id, Runnable task, int ticksDelay, int ticksInterval, String help){
            this.task = task;
            startAt = ticks + ticksDelay;
            interval = ticksInterval;
            this.id = id;
            this.help = help;
        }


        public Task(long id, Runnable task, int ticksDelay, int ticksInterval, long times){
            this(id,task,ticksDelay,ticksInterval);
            timesLeft = times;
        }


        public boolean isFinished(){
            return timesLeft==0;
        }


        public boolean tryRun(){

            if(ticks >= startAt){
                try{
                    task.run();
                }catch(Exception e){
                    System.out.println("Something going wrong at task #"+id+":\n"+e.getMessage()+" \n("+help+")");
                }
                startAt = ticks+interval;
                timesLeft--;
                if(isFinished()){
                    Scheduler.cancelTask(id);
                }
                return true;
            }

            return false;
        }
    }

}
