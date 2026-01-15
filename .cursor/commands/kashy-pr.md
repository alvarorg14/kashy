# Smart PR Command (Kashy)

This command generates a concise, technical PR description from the diff between the current branch and its parent branch, following Kashy’s OSS contribution guidelines and PR template conventions.

## Usage
- Type `/create-pr-description` to produce a PR-ready title, labels, and summary.
- Determine the parent branch with:
  `git reflog --all | grep "$(git branch --show-current)" | tail -1`
  Then diff that branch against the current one.

## What This Command Does
1. **Identify Base**: Detect the branch the current one was created from (fallback to `main` if unknown).
2. **Collect Changes**: Compute the diff between the base branch and `HEAD`.
3. **Classify Changes (Kashy model)**:
   - Select exactly **one** `type:*` label based on the diff.
   - Select at least **one** `area:*` label based on touched modules/paths.
   - Optionally add release labels (`breaking change`, `dependencies`, `skip-changelog`) if applicable.
4. **Produce Outputs**:
   - Suggested PR title(s) using semantic type + optional scope + description.
   - Suggested labels (Kashy labels, not GitHub defaults).
   - A Markdown summary ready to paste into the PR description.

## IMPORTANT: Kashy PR Label Policy (must enforce)
- Every PR must have:
  - **Exactly 1** label starting with `type: `
  - **At least 1** label starting with `area: `
- PRs should **not** use `status:*` labels (those are for issues).
- Use `breaking change` when the API/behavior is not backward compatible.
- Use `dependencies` when the PR primarily updates dependencies (e.g., Maven versions, plugin bumps).
- Use `skip-changelog` for internal changes that should not appear in release notes (typos, non-user-facing CI tweaks, etc.).

## Kashy Label Mapping (how to choose labels)

### Type label (choose exactly one)
- `type: feature` — new user-facing functionality
- `type: bug` — bug fix
- `type: enhancement` — improvement to existing functionality
- `type: documentation` — docs-only changes
- `type: refactor` — structural/internal change, no behavior change
- `type: maintenance` — tooling/CI/build housekeeping
- `type: security` — security fix/hardening

### Area labels (choose 1+)
Infer from touched paths:
- `kashy-api/**` → `area: api`
- `kashy-agent/**` → `area: agent`
- `ashy-messaging/**` → `area: messaging`
- `kashy-shared/**` → `area: shared`
- `infra/**` or `.github/**` → `area: infra`
- `docs/**` or `*.md` (non-template) → `area: docs`
- `pom.xml` or maven/plugin changes → `area: build`

### Optional PR/release labels
- `breaking change` — non-backward-compatible change (must mention in summary)
- `dependencies` — dependency updates
- `skip-changelog` — exclude from release notes

## Output Structure (must match)
- **Suggested PR titles**: 1–2 options following `<type>(<scope>): <concise description>`.
  - The `<type>` must match the chosen `type:*` label:
    - `type: feature` → `feat`
    - `type: bug` → `fix`
    - `type: enhancement` → `enhance`
    - `type: documentation` → `docs`
    - `type: refactor` → `refactor`
    - `type: maintenance` → `chore`
    - `type: security` → `security`
  - `<scope>` should be one of: `api`, `agent`, `messaging`, `shared`, `infra`, `build`, `docs` when obvious from the diff; omit if unclear.
  - Print title suggestions each on a new line for easy copying.
- **Suggested labels**: output Kashy labels only (no GitHub default labels).
- **PR summary**: provided as a single code block.

## PR Summary Content Rules (must follow `.github/PULL_REQUEST_TEMPLATE.md`)
- The PR summary output must be formatted to match the repository’s PR template located at `.github/PULL_REQUEST_TEMPLATE.md`.
- Use the template’s section headings, order, and checkbox items exactly as defined in that file.
- If the template contains placeholders (e.g., `<!-- ... -->`), replace them with concise, technical content derived from the diff.
- If a section in the template is not applicable, follow the template’s instructions for that case (e.g., leave it empty, write “N/A”, or remove it) — do not invent a new convention.
- Keep language technical and concise; focus on user-visible behavior, API changes, and notable internal architecture changes.
- Reference code artifacts in backticks (`ExpenseService`, `V1__init.sql`, etc.).
- Mention tests only when they affect execution/behavior or add meaningful coverage; otherwise keep test mentions minimal.
- If the PR has the `breaking change` label (or the diff indicates a breaking change), ensure the template’s breaking-change / migration section (if present) is filled with explicit migration notes.
- Do not include secrets, tokens, private keys, or sensitive data in the summary.
- Include language-tagged code blocks only when a snippet materially clarifies a change and the template permits it.

## Template Compliance Requirements
- Do not introduce headings that are not present in `.github/PULL_REQUEST_TEMPLATE.md`.
- Do not use higher-level headings than the template uses.
- Preserve checkbox formatting (`- [ ]`) exactly as in the template.
- Preserve any required fields the template expects (e.g., “Type of change”, “Related issues”, “How to test”, etc.).

## Formatting Requirements (strict)
- Put the final PR summary inside a single fenced code block.
- Titles and labels are part of the agent response; the summary is the final payload.

---