## Unreleased

### Added
- Persistent tax treasury for taxes collected from player shops, orders, and `/sell`.
- Equal tax redistribution across eligible players.
- Offline redistribution support with pending payout notices shown on next login.
- Treasury admin commands:
  - `/eco treasury`
  - `/eco treasury add <amount>`
  - `/eco treasury set <amount>`
  - `/eco treasury clear`
  - `/eco treasury payout`
- Payout broadcast messages when treasury redistribution runs.
- `taxRedistributionMinimumAmount` config option for skipping scheduled payouts until the treasury reaches a configured minimum.
- Paper cheques with `/cheque <amount>` and `/cheque redeem`.
- New tax redistribution config options:
  - `taxRedistributionEnabled`
  - `taxRedistributionIntervalTicks`
  - `taxRedistributionOnlineOnly`

### Changed
- Retargeted the NeoForge build to Minecraft 1.21.1 / NeoForge 21.1.217 / Architectury 13.0.8.
- Updated common dependencies so shared code builds against the selected Minecraft target.
- Changed default redistribution eligibility to all known balances by setting `taxRedistributionOnlineOnly` to `false`.
- Kept redistribution equal and drama-free; no contribution tracking, weighted payouts, taxpayer rankings, or per-player tax statistics were added.

### Fixed
- Fixed 1.21.1 runtime compatibility issues caused by newer Minecraft mappings.
- Fixed 1.21.1 operator permission checks by using the older-compatible player game profile API.
- Fixed standalone admin commands not appearing even when enabled in the config.
- Fixed admin commands appearing for non-operators.
