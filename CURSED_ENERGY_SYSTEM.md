# Cursed Energy System Documentation

## Overview
A complete cursed energy management system for the Jujutsu Sorcery mod, featuring:
- NBT-based persistent player data
- Network synchronization between server and client
- Animated HUD display showing cursed energy status
- Fully organized file structure

## File Structure

```
src/main/java/com/anastas1s12/jjs/
├── cursed_energy/
│   ├── CursedEnergyData.java          # Data class for storing CE info
│   ├── CursedEnergyManager.java       # Server-side manager
│   └── CursedEnergyNetworking.java    # Networking handler
├── networking/
│   └── payload/
│       └── SyncCursedEnergyPayload.java # Network packet definition
├── client/
│   ├── hud/
│   │   ├── CursedEnergyHUD.java       # HUD initialization & callbacks
│   │   └── CursedEnergyRenderer.java  # Rendering logic
│   └── network/
│       └── ClientCursedEnergyManager.java # Client-side data manager
└── mixin/
    └── PlayerEntityMixin.java          # Mixin for accessing player data

src/main/resources/
└── jjs.mixins.json                     # Mixin configuration
```

## Core Components

### 1. CursedEnergyData
**Location:** `cursed_energy/CursedEnergyData.java`

Stores player cursed energy information:
- `currentCE`: Current cursed energy (0 - maxCE)
- `maxCE`: Maximum cursed energy (1-100000)

**Key Methods:**
- `addCE(int amount)` - Add CE to player
- `removeCE(int amount)` - Remove CE from player
- `setMaxCE(int maxCE)` - Set max CE with cap
- `getPercentage()` - Get CE as percentage (0.0-1.0)
- `writeToNbt(NbtCompound)` - Serialize to NBT
- `readFromNbt(NbtCompound)` - Deserialize from NBT

### 2. CursedEnergyManager
**Location:** `cursed_energy/CursedEnergyManager.java`

Server-side management of cursed energy:
- Handles player data persistence via NBT
- Automatically syncs data to clients on join
- Provides static methods for CE manipulation

**Public Methods:**
```java
CursedEnergyManager.initialize();                    // Call in main mod init
CursedEnergyManager.getCursedEnergyData(player);     // Get player CE data
CursedEnergyManager.addCursedEnergy(player, amount); // Add CE
CursedEnergyManager.removeCursedEnergy(player, amount); // Remove CE
CursedEnergyManager.setMaxCursedEnergy(player, maxCE); // Set max CE
```

### 3. SyncCursedEnergyPayload
**Location:** `networking/payload/SyncCursedEnergyPayload.java`

Network packet for syncing CE data:
- Packet ID: `jjs:sync_cursed_energy`
- Contains: currentCE, maxCE
- Automatically sent when player joins or CE changes

### 4. CursedEnergyRenderer
**Location:** `client/hud/CursedEnergyRenderer.java`

Renders the HUD display:
- 16x16 animated icon (scaled 2x to 32x32)
- Purple bar showing CE percentage
- Text display: current/max CE and percentage
- Located in top-left corner (HUD_X=10, HUD_Y=10)

**Display Layout:**
```
┌─────────────────────┐
│ [CE Bar] CE: 1,000  │
│ 32x256  / 1,000    │
│ (32x256 50.0%      │
│  scaled │●●●●●●●●●│
│  2x)    │         │
│         │         │
└─────────────────────┘
```

### 5. CursedEnergyHUD
**Location:** `client/hud/CursedEnergyHUD.java`

HUD lifecycle management:
- Registers HUD render callback on client init
- Updates display with client-side data
- Handles disconnection cleanup

### 6. ClientCursedEnergyManager
**Location:** `client/network/ClientCursedEnergyManager.java`

Client-side network handling:
- Registers packet handler for `SyncCursedEnergyPayload`
- Updates HUD display when data arrives
- Stores current player CE data

## Usage Examples

### Add Cursed Energy to Player
```java
CursedEnergyManager.addCursedEnergy(player, 500);
```

### Remove Cursed Energy
```java
CursedEnergyManager.removeCursedEnergy(player, 250);
```

### Get Current CE
```java
CursedEnergyData data = CursedEnergyManager.getCursedEnergyData(player);
int currentCE = data.getCurrentCE();
int maxCE = data.getMaxCE();
float percentage = data.getPercentage();
```

### Increase Max CE
```java
CursedEnergyManager.setMaxCursedEnergy(player, 5000);
```

## Constants

**CursedEnergyData Constants:**
- `MIN_CE` = 0
- `DEFAULT_MAX_CE` = 1000
- `MAX_CE_LIMIT` = 100000

**Renderer Constants:**
- Icon size: 16x16
- Display scale: 2x (32x32)
- HUD position: (10, 10)
- Animation speed: 8 ticks per frame

## NBT Storage

Cursed energy data is stored in player persistent data:
```
Player NBT
├─ CursedEnergyData (CompoundTag)
│  ├─ CurrentCE (IntTag)
│  └─ MaxCE (IntTag)
```

## Networking

**Sync Packet Flow:**
1. Server detects CE change (addCursedEnergy, removeCursedEnergy, etc.)
2. Server sends `SyncCursedEnergyPayload` to client
3. Client receives packet via registered handler
4. HUD display updates automatically

**Automatic Sync on Join:**
- When player joins server, `CursedEnergyManager` automatically syncs their CE data
- No manual intervention needed

## Initialization

The system is automatically initialized:

**Server-side** (in `JujutsuSorcery.onInitialize()`):
```java
CursedEnergyManager.initialize();
```

**Client-side** (in `JujutsuSorceryClient.onInitializeClient()`):
```java
ClientCursedEnergyManager.initialize();
CursedEnergyHUD.initialize();
```

## Mixin Integration

The system uses a mixin to access player persistent NBT data:

**PlayerEntityMixin.java:**
- Provides accessor to `persistentData` field
- Allows reading/writing CE data to player NBT
- Registered in `jjs.mixins.json`

## Future Enhancements

Potential additions:
1. Custom texture rendering for CE icon
2. Animation frame handling for visual effects
3. Sound effects for CE gain/loss
4. CE bar tooltips with detailed info
5. CE drain over time mechanics
6. Multi-player CE tracking
7. CE level restrictions for abilities

## Troubleshooting

**HUD not showing:**
- Ensure `ClientCursedEnergyManager.initialize()` is called
- Check network packets are being received (add debug logging)
- Verify player has CE data (should auto-create on join)

**Data not persisting:**
- Check NBT storage path: "CursedEnergyData"
- Verify `PlayerEntityMixin` is properly registered
- Ensure `CursedEnergyManager.initialize()` is called on server

**Network sync issues:**
- Check mod ID in payload matches: "jjs"
- Ensure `SyncCursedEnergyPayload.TYPE` is properly defined
- Verify both server and client have same packet ID

