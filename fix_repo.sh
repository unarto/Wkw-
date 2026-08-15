#!/bin/bash
for file in core-storage/src/main/kotlin/com/wakwau/xplore/core/storage/repository/*.kt; do
  sed -i 's/catch (e: Exception)/catch (e: kotlinx.coroutines.CancellationException) { return@withContext FileOperationResult.Cancelled } catch (e: Exception)/g' "$file"
done
