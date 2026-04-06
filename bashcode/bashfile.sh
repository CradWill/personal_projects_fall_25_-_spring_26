#!/bin/bash

DIR="${1:-.}"
N="${2:-10}"

# thresholds (in bytes)
THRESH_GIG=$((1024*1024*1024))    # 1 GiB
THRESH_MB=$((100*1024*1024))      # 100 MiB

# categorized arrays
declare -a Huge_Files Large_Files Medium_Files
declare -a SIZE_BYTES PATHS

mapfile -t TOPN < <(
    find "$DIR" -type f -printf '%s %p\n' \
    | sort -nr \
    | head -n "$N"
)

for line in "${TOPN[@]}"; do
    # correctly read size and path (space-separated)
    read -r sz path <<< "$line"

    SIZE_BYTES+=("$sz")
    PATHS+=("$path")

    if (( sz >= THRESH_GIG )); then
        Huge_Files+=("$path")
    elif (( sz >= THRESH_MB )); then
        Large_Files+=("$path")
    else
        Medium_Files+=("$path")
    fi
done

printf 'Top %d largest files under: %s\n' "$N" "$DIR"
printf 'Bytes\tPath\n'
printf '-----\t----\n'

for i in "${!PATHS[@]}"; do
    printf '%s\t%s\n' "${SIZE_BYTES[i]}" "${PATHS[i]}"
done

echo
echo "Category Counts:"
printf 'Huge   (>= 1 GiB):   %d\n' "${#Huge_Files[@]}"
printf 'Large  (>= 100 MiB): %d\n' "${#Large_Files[@]}"
printf 'Medium (< 100 MiB):  %d\n' "${#Medium_Files[@]}"