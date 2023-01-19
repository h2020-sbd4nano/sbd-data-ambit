name: Extract Bioschemas

on:
  workflow_dispatch:
  schedule:
    - cron: "0 0 * * 0"
  push:
    paths:
      - '*.groovy'

jobs:
  webpage:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
        with:
          submodules: true
          fetch-depth: 0

      - name: Install groovy
        run: sudo apt install groovy

      - name: Generate website
        run: |
          groovy extractBioschemasOpen.groovy | tee open-licensed.ttl
          groovy extractBioschemas.groovy | tee closed-licensed.ttl

      - name: Commit report
        run: |
          git config --global user.name 'GitHub Action'
          git config --global user.email 'action@github.com'
          git add docs/service/*.md
          if git diff --exit-code --staged; then
              echo "No changes"
          else
              git commit -m 'Extracted new Bioschemas data as Turtle'
              git push
          fi
