# railway-cargo-tracker

Determines possible cargo types at each station in a directed railway network using BFS fixed-point iteration.

## Problem

A railway system consists of stations connected by one-way tracks. Each station unloads one cargo type on arrival and loads another before departure. All trains start from the same station carrying nothing.

Given the network, determine for each station which cargo types might be on a train when it arrives.

## Usage

```bash
# From file
./railway-cargo-tracker --input network.txt --output result.txt

# stdin/stdout
cat network.txt | ./railway-cargo-tracker
```

## Input Format

```
S T
s1 c_unload c_load
s2 c_unload c_load
...
s_from s_to
s_from s_to
...
s_start
```

- First line: number of stations `S` and tracks `T`
- Next `S` lines: station id, cargo type unloaded, cargo type loaded
- Next `T` lines: directed track from one station to another
- Last line: starting station id

## Output Format

```
Station 1: [2, 5, 7]
Station 2: [3]
Station 3: []
```

One line per station (sorted by id), listing possible arriving cargo types (sorted ascending).

## Build

```bash
./gradlew build
./gradlew installDist
```

## Test

```bash
./gradlew test
```
