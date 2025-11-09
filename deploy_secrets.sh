#!/bin/bash

# This script creates or updates Docker Swarm secrets.
# It should be run from the root of your project.

set -e # Exit immediately if a command fails

SECRET_DIR="./secrets/prod"
echo "Starting secret deployment..."

# Find all .txt files in the secret directory
for secret_file in $(find "$SECRET_DIR" -type f -name "*.txt"); do
    # Get the filename without the .txt extension (e.g., "db_password")
    secret_name=$(basename "$secret_file" .txt)

    echo "Processing secret: $secret_name"

    # Remove the secret if it already exists (to update it)
    if docker secret inspect "$secret_name" >/dev/null 2>&1; then
        echo "  -> Secret '$secret_name' already exists. Removing to update."
        docker secret rm "$secret_name"
    fi

    # Create the new secret from the file
    docker secret create "$secret_name" "$secret_file"
    echo "  -> Secret '$secret_name' created."
done

echo "Secret deployment finished."