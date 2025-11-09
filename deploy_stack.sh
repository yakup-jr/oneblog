#!/bin/bash

# This script deploys your Docker Swarm stack.
# It should be run from the root of your project.

set -e # Exit immediately if a command fails

# --- CONFIGURATION ---
# Set your stack name here
STACK_NAME="oneblog"

# Directory where your compose files are located
COMPOSE_DIR="./docker/prod"
# ---------------------

# Default compose file
DEFAULT_COMPOSE_FILE="docker-compose.yml"

# Use the first argument as the compose file name, or use the default
COMPOSE_FILE_NAME=${1:-$DEFAULT_COMPOSE_FILE}

COMPOSE_FILE_PATH="$COMPOSE_DIR/$COMPOSE_FILE_NAME"

# Check if the compose file actually exists
if [ ! -f "$COMPOSE_FILE_PATH" ]; then
    echo "Error: Compose file not found at $COMPOSE_FILE_PATH"
    echo "Usage: ./deploy_stack.sh [compose-file-name.yml]"
    exit 1
fi

echo "Deploying stack '$STACK_NAME' using file '$COMPOSE_FILE_PATH'..."

# Deploy the stack
docker stack deploy -c "$COMPOSE_FILE_PATH" "$STACK_NAME"

echo "Stack deployment command sent."
echo "Run 'docker stack ps $STACK_NAME' to monitor the services."