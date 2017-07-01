## v2.0

- From v2.0 and onwards, I will provide a list of changes and additions in this file, `CHANGELOG.md`.

Changes:

- Refine how a `WatTransaction` is categorized
    - `account` field is regarded as `int` instead of `String`
    - New `balanceType` stores the `WatBalanceType` of the transaction (the balance account from/to which funds were taken/added)
- Add additional `WatBalanceType` for preventative measures