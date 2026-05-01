# EconomyCraft

EconomyCraft provides a simple server-side cross-platform economy system for Fabric and NeoForge servers.
The mod requires Architectury API and targets **Minecraft 1.21.x**.

## Feature List

- Persistent player balances stored server-side.
- Balance commands with player lookup and top balance display.
- Player-to-player payments.
- Daily claim rewards.
- Paper cheques that let players withdraw money into tradeable items and redeem them later.
- Player marketplace with item listings.
- Request/order marketplace where players can pay others to supply items.
- Server shop with configurable prices.
- `/sell` command for selling items directly to the server.
- Daily sell limit support.
- Transaction tax support for shops, orders, and `/sell`.
- Persistent tax treasury that stores collected tax across restarts.
- Equal tax redistribution on a configurable server-tick interval.
- Offline tax redistribution support with pending payout notices on next login.
- Treasury admin commands for viewing, adding, setting, clearing, and forcing payouts.
- Payout broadcasts when treasury redistribution runs.
- Optional minimum treasury amount before scheduled payouts.
- Optional PvP balance loss transfer.
- Optional balance scoreboard.
- Configurable standalone command behavior.

## Update List

- Retargeted the NeoForge build to Minecraft 1.21.1 to avoid newer-mapping runtime crashes on 1.21.1 servers.
- Added persistent tax redistribution with offline player support.
- Added treasury admin controls and payout broadcasts.
- Added tax collection to `/sell`.
- Added minimum payout threshold support.
- Added cheque creation and redemption commands.
- Preserved equal redistribution only; no contribution tracking, weighted payouts, taxpayer rankings, or per-player tax statistics were added.

## Commands

### Player Commands

- `/bal [<player|selector>|top]` - Check balances or view the top balances.
- `/pay <player> <amount>` - Transfer money to another player.
- `/cheque <amount>` - Withdraw money into a paper cheque.
- `/cheque redeem` - Redeem the cheque held in your main hand.
- `/daily` - Claim a daily login bonus.
- `/shop` - Player-driven marketplace where players list items for sale.
- `/shop list <price>` - List the item in your hand.
- `/servershop` - Server-managed shop with unlimited supply. Prices can be edited in `config/prices.json`.
- `/sell [<amount>|all]` - Sell the item in your hand. Use `all` to sell all matching items from your inventory.
- `/orders` - Request-based trading system.
- `/orders request <item> <amount> <price>` - Create an item request.
- `/orders claim` - Claim items bought or requested while offline.

### Admin Commands

- `/eco addmoney <player|selector> <amount>` - Add money to a player.
- `/eco setmoney <player|selector> <amount>` - Set a player's balance.
- `/eco removemoney <player|selector> [amount]` - Remove money from a player.
- `/eco removeplayer <player|selector>` - Remove a player from the economy system.
- `/eco toggleScoreboard` - Toggle the balance sidebar for all players.
- `/eco treasury` - View the tax treasury.
- `/eco treasury add <amount>` - Add money to the tax treasury.
- `/eco treasury set <amount>` - Set the tax treasury balance.
- `/eco treasury clear` - Clear the tax treasury.
- `/eco treasury payout` - Force an immediate equal treasury payout.

**Notes:**

- Non-admin commands such as `/pay` or `/daily` are standalone by default and also work under `/eco`, for example `/eco pay`.
- Set `standalone_commands` to `false` in `config.json` to require the `/eco` prefix.
- Admin commands use `/eco` unless `standalone_admin_commands` is enabled.

## Configuration

Configuration and player data are stored in `config/economycraft/`.
Runtime treasury data is stored in `config/economycraft/data/treasury.json`.

### Default `config.json`

```json
{
  "startingBalance": 1000,
  "dailyAmount": 100,
  "dailySellLimit": 10000,
  "taxRate": 0.1,
  "taxRedistributionEnabled": true,
  "taxRedistributionIntervalTicks": 168000,
  "taxRedistributionOnlineOnly": false,
  "taxRedistributionMinimumAmount": 0,
  "pvp_balance_loss_percentage": 0.0,
  "standalone_commands": true,
  "standalone_admin_commands": false,
  "scoreboard_enabled": true,
  "server_shop_enabled": true
}
```

- `startingBalance` - initial money for new players. Default: `1000`.
- `dailyAmount` - money given by `/daily`. Default: `100`.
- `dailySellLimit` - maximum money a player can earn per day via selling. `0` disables the limit. Default: `10000`.
- `taxRate` - percentage tax applied to trades, orders, and `/sell` as a decimal factor. Example: `0.1` means 10%.
- `taxRedistributionEnabled` - enables scheduled tax treasury redistribution. Default: `true`.
- `taxRedistributionIntervalTicks` - server ticks between scheduled treasury payouts. Default: `168000`.
- `taxRedistributionOnlineOnly` - when `true`, only online players are eligible. When `false`, all known balances are eligible and offline players receive pending payout notices next login. Default: `false`.
- `taxRedistributionMinimumAmount` - minimum treasury balance required before a scheduled payout runs. `0` disables the threshold. Default: `0`.
- `pvp_balance_loss_percentage` - percentage of a player's balance lost on PvP death and transferred to the killer as a decimal factor. `0` disables this feature.
- `standalone_commands` - enable standalone `/pay`, `/daily`, etc. Default: `true`.
- `standalone_admin_commands` - enable standalone `/addmoney`, `/setmoney`, etc. Default: `false`.
- `scoreboard_enabled` - show the balance sidebar by default. Can be toggled with `/eco toggleScoreboard`. Default: `true`.
- `server_shop_enabled` - enables the server shop with `/servershop` and `/eco servershop`. Default: `true`.
