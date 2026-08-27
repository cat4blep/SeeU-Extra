# SeeU Extra

SeeU Extra renders configured non-player entities beyond vanilla tracking distance through [SeeU](https://github.com/cat4blep/SeeU). It supports Fabric and NeoForge modded servers. Paper remains player-only.

Install matching SeeU and SeeU Extra builds on the server and every client. This branch requires SeeU 0.9.1 or newer, but earlier than 1.0.0. Downloads are available on the [releases page](https://github.com/cat4blep/SeeU-Extra/releases).

## Configuration

The server creates `config/seeu-extra-server.json`. Its default mode is `DISABLED`, so the addon does not scan entities until an administrator enables it.

- `SELECTED` accepts entity IDs from `types` and mod or registry namespaces from `namespaces`.
- `ALL` accepts every eligible loaded non-player entity.
- `excludedTypes` and `excludedNamespaces` take precedence in both modes.
- `maximumDistanceBlocks`, `minimumDistanceBlocks`, `entityCap`, and `updateIntervalTicks` limit server work and traffic.

```json
{
  "configVersion": 1,
  "mode": "SELECTED",
  "types": ["minecraft:zombie"],
  "namespaces": ["iceandfire"],
  "excludedTypes": [],
  "excludedNamespaces": [],
  "maximumDistanceBlocks": 8192,
  "minimumDistanceBlocks": 0,
  "entityCap": 128,
  "updateIntervalTicks": 4
}
```

The client creates `config/seeu-extra-client.json` with its enable switch and distance limits. The server uses the lower maximum distance and the higher minimum distance from the two configurations. Restart the client or server after editing either file.

## Limits

- The addon reads loaded entities and never loads chunks.
- Players and entities carrying a player stay on SeeU's player path.
- Each client needs the mod that registers every selected entity type.
- Position, rotation, velocity, pose, flags, and equipment are synchronized. Renderers that require custom tracked data can still differ from the server entity.

## Supported versions

| Branch | Minecraft | Java | Loaders |
| --- | --- | --- | --- |
| `main` | 26.2 | 25 | Fabric, NeoForge |
| `multiloader-26.1.2` | 26.1.2 | 25 | Fabric, NeoForge |
| `multiloader-1.21.11` | 1.21.11 | 21 | Fabric, NeoForge |
| `multiloader-1.21.1` | 1.21.1 | 21 | Fabric, NeoForge |

## License

See [LICENSE](LICENSE).
