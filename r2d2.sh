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


falconJsonFile=$1
empireJsonFile=$2
# Convert relative path to absolute path
if [[ ! "$falconJsonFile" = /* ]]; then
    jsonfile="$PWD/$1"
fi

# Extract routes_db property
routes_db=$(jq -r '.routes_db' "$falconJsonFile")


# Check if routes_db path is relative or absolute
if [[ ! "$routes_db" = /* ]]; then
    # Convert relative path to absolute path
    dir_of_jsonfile=$(dirname "$falconJsonFile")
    SQLDB_PATH="$dir_of_jsonfile/$routes_db/"
else
    SQLDB_PATH="$routes_db"
fi

MODIFIED_JSON=$(jq --arg key "routes_db" --arg value "$SQLDB_PATH" '.[$key]=$value' "$falconJsonFile")

echo $MODIFIED_JSON

java -Dspring.profiles.active=cli -Dspring.application.json="$MODIFIED_JSON" -jar cli/millenium-falcon-app-1.0-SNAPSHOT.jar "$empireJsonFile"