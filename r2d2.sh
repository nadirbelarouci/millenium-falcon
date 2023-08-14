#!/usr/bin/env bash

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <path_to_millenium_falcon_file> <path_to_empire_file>"
    exit 1
fi

# Check if jq is installed
if ! command -v jq &> /dev/null; then
    echo "Error: jq is not installed. Please install jq to proceed."
    exit 1
fi

FALCON="$1"
EMPIRE="$2"

falconDir=$(dirname "$FALCON")
SQLDB_PATH="$PWD/$falconDir/$(jq -r '.routes_db' "$FALCON" )"
MODIFIED_JSON=$(jq --arg key "routes_db" --arg value "$SQLDB_PATH" '.[$key]=$value' "$FALCON")

echo $MODIFIED_JSON


java -Dspring.profiles.active=cli -Dspring.application.json="$MODIFIED_JSON" -jar cli/millenium-falcon-app-1.0-SNAPSHOT.jar "$EMPIRE"