# Minecraft Forge Prometheus Exporter
A Forge mod which exports minecraft 1.12.x server stats to Prometheus. Based on [Minecraft Prometheus Exporter for Bukkit](https://github.com/sladkoff/minecraft-prometheus-exporter)

## Quick Start
Drop the prometheusexporter-1.0.jar into your mods directory and start your Minecraft Forge server.

After startup, the Prometheus metrics endpoint should be available at localhost:9225.

The metrics port can be customized in the mods's config.cfg (a default config will be created after the first use).

## Metrics command
Everyone can use /metrics command, which shows the average TPS, online players, allocated memory and the number of loaded chunks

## Default config
```
# Configuration file

~CONFIG_VERSION: 1.0

export {
    I:"Prometheus export port"=9225
    I:"Ticks between collecting online players"=100
    I:"Ticks between collecting performance"=40
    I:"Ticks between collecting players statistics"=1200
}


metrics {
    B:"Collect JVM memory usage metrics"=true
    B:"Collect loaded chunks"=true
    B:"Collect online players count"=true
    B:"Collect online players names"=true
    B:"Collect online players statistics"=true
    B:"Collect server TPS metrics"=true
}
```
