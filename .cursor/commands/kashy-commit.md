# Smart Commit Command (Kashy)

This command analyzes uncommitted changes and creates multiple logical, focused commits following **Conventional Commits v1.0.0** (https://www.conventionalcommits.org/en/v1.0.0/). It is designed for open-source workflows: small, reviewable commits, clear history, and automation-friendly messages.

## Usage
Type `/kashy-commit` to automatically organize and commit your changes.

## What This Command Does
1. **Analyze Changes**: Review all uncommitted changes using `git status` and `git diff`.
2. **Stage Intentionally**:
   - If there are unstaged changes, show them and ask whether to stage them.
   - If the user declines, proceed with already-staged changes only.
3. **Group Logically**: Split changes into multiple commits when it improves clarity, e.g.:
   - Features vs fixes vs refactors
   - Production code vs tests
   - Docs vs build/config
4. **Determine Conventional Commit Type + Scope**:
   - Choose the best `type` and optional `scope` based on changed paths/modules.
5. **Detect Breaking Changes**:
   - If a commit includes a breaking change, use `!` after type/scope and/or include `BREAKING CHANGE:` in the body.
6. **Format Code (when available)**:
   - If the repo contains Spotless configuration, run `mvn spotless:apply` before committing.
7. **Create Focused Commits**:
   - Stage only relevant files per group and commit with a compliant message.
8. **Provide Summary Output**:
   - Print created commits (type/scope + short description).

## Conventional Commit Message Format (required)
**Header**
- Template: `<type>(<scope>)!: <description>`
- Where:
  - `<type>` is required
  - `<scope>` is optional
  - `!` is optional and indicates a breaking change
  - `<description>` is required, imperative mood, present tense, lowercase start preferred

**Body (optional)**
- Explain “what” and “why”, not implementation details.

**Footer (optional but required for breaking changes if not using `!`)**
- Use:
  - `BREAKING CHANGE: <explanation and migration notes>`
- You may also include issue references in the footer, e.g. `Refs: #123` or `Closes: #123`.

## Allowed Types (Kashy)
Use the Conventional Commits standard types commonly used in OSS:

- `feat` — new functionality
- `fix` — bug fix
- `docs` — documentation only
- `style` — formatting, whitespace, no code behavior change (use sparingly; prefer running formatter)
- `refactor` — refactor without behavior change
- `test` — adding/updating tests only
- `chore` — maintenance tasks (tooling, CI, infra, non-prod changes)
- `build` — build system or dependency changes (Maven, plugins)
- `ci` — CI pipeline/workflow changes

> Rule of thumb:
> - Dependency bumps → `build` (or `chore` if purely housekeeping; prefer `build` for Maven changes)
> - GitHub Actions changes → `ci`
> - Docker / compose / infra scripts → `chore(infra)` or `ci` depending on purpose

## Scope Rules (recommended)
- Scope should be short and stable. Prefer module/service scopes:
  - `api`, `agent`, `messaging`, `shared`, `infra`, `build`, `docs`
- If change is cross-cutting, omit scope.
- If change is very localized and helpful, a sub-scope is OK:
  - `api-security`, `api-expenses`, `agent-tools`, etc. (keep consistent)

## Mapping Changed Files → Suggested Scope
- `kashy-api/**` → `scope: api`
- `kashy-agent/**` → `scope: agent`
- `kashy-messaging/**` → `scope: messaging`
- `kashy-shared/**` → `scope: shared`
- `infra/**` or `.github/**` → `scope: infra` (or `ci` type for workflows)
- `docs/**` or `*.md` → `scope: docs`
- `pom.xml`, `**/pom.xml`, Maven plugin/version changes → `scope: build`

## Change Categorization Logic (how to split commits)
Split into multiple commits when there are clearly distinct concerns, such as:
- `feat` vs `fix` in different areas
- production code vs tests
- docs-only changes
- build/dependency changes separated from code changes
- refactors separated from functional changes (unless inseparable)

### Recommended grouping order
1. `build` / `ci` (if they must be applied before tests/build pass)
2. `feat` / `fix`
3. `refactor`
4. `test`
5. `docs`
6. `chore` (misc)

## Implementation Steps (detailed)
1. **Check Git Status**
   - Run `git status --porcelain`
   - Identify staged vs unstaged vs untracked files
2. **Analyze Diffs**
   - Use `git diff --cached` for staged
   - Use `git diff` for unstaged
3. **Interactive Staging**
   - If unstaged/untracked exist:
     - List them
     - Ask: “Stage all unstaged files? (y/n)”
     - If yes: `git add -A`
     - If no: proceed with staged changes only
4. **Detect Spotless**
   - If the repo has Spotless configured (e.g., plugin present in `pom.xml`), run:
     - `mvn spotless:apply`
   - If it changes files, re-run staging decision for modified files (stage them appropriately)
5. **Group Files**
   - Group by module + category (prod/test/docs/build/ci/infra)
6. **Create Commits**
   For each group:
   - Stage only that group’s files (`git add <paths>`)
   - Compose a conventional commit message:
     - `type(scope): description`
     - Add `!` if breaking
     - Add `BREAKING CHANGE:` footer when needed
   - Commit
7. **Final Check**
   - Ensure working tree is clean or report remaining unstaged changes clearly

## Commit Message Examples (Kashy)
- `feat(api): add subscriptions CRUD endpoints`
- `fix(agent): handle missing currency in expense parsing`
- `refactor(shared): extract money parsing utility`
- `test(api): add integration tests for expense filters`
- `docs: document local docker-compose setup`
- `build: bump spring boot to 3.3.x`
- `ci: enforce pr label policy workflow`
- `feat(api)!: rename expense amount field to value`
  - Body/footer should include:
    - `BREAKING CHANGE: expense.amount renamed to expense.value; clients must update DTO mapping`

## Error Handling / Guardrails
- If there are **no staged changes** and user refuses staging, exit with a clear message.
- Never commit secrets (keys, tokens, `.env` with credentials); warn if suspicious files are detected.
- Avoid mixing unrelated concerns in one commit unless unavoidable.
- Handle merge conflicts gracefully (abort and explain).

## Advanced Features (optional)
- Ask for confirmation before committing each group (interactive review mode).
- Support `Closes: #123` when the diff clearly references an issue.
- Auto-detect breaking changes when:
  - public API signatures change
  - DTO field removals/renames occur
  - endpoint paths change
  (When detected, require `!` or `BREAKING CHANGE:` footer.)