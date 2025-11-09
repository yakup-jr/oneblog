#!/bin/bash

# This script creates or updates Docker Swarm configs.
# It should be run from the root of your project.

set -e # Exit immediately if a command fails

CONFIG_DIR="./config/prod"
echo "Starting config deployment..."

# --- FIX: Changed 'for' loop to 'find | while read' to handle spaces in file names ---
find "$CONFIG_DIR" -type f \( -name "*.cfg" -o -name "*.yml" \) | while IFS= read -r config_file; do
    # Get the filename
    filename=$(basename "$config_file")

    # --- NEW EXCLUSION LOGIC ---
    case "$filename" in
        "haproxy.cfg"|"haproxy(no logs).cfg")
            echo "Skipping excluded config: $filename"
            continue
            ;;
    esac
    # --- END NEW EXCLUSION LOGIC ---

    # 1. Remove the extension (e.g., "haproxy(no logs)" from "haproxy(no logs).cfg")
    config_name_raw="${filename%.*}"

    # 2. Sanitize the name: replace spaces and parentheses with hyphens (-)
    # to comply with Docker's restrictive naming rules (e.g., "haproxy-no-logs")
    config_name=$(echo "$config_name_raw" | tr ' ()' '-')

    echo "Processing config: $config_name"

    # Remove the config if it already exists (to update it)
    if docker config inspect "$config_name" >/dev/null 2>&1; then
        echo "  -> Config '$config_name' already exists. Removing to update."
        docker config rm "$config_name"
    fi

    # Create the new config from the file
    docker config create "$config_name" "$config_file"
    echo "  -> Config '$config_name' created."
done
# --- END OF FIX ---

echo "Config deployment finished."