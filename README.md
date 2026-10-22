# AudioWire — MRI Audio Workflow POC

A hands-on Java proof of concept exploring a command-driven audio workflow inspired by MRI imaging systems.

AudioWire demonstrates how a client can communicate with an audio service over TCP, load audio message-set configuration from XML, and trigger pre-scan and post-scan WAV playback.

> **Disclaimer:** This is an independent educational proof of concept. It is not an official GE Healthcare project, a reproduction of proprietary GE software, or a medical-device-ready implementation.

## 1. Objectives

The objectives of this POC are to understand and demonstrate:

- TCP client-server communication using Java sockets.
- Multithreaded client connection handling.
- Command-based request-response processing.
- XML-based audio message-set configuration.
- WAV audio playback using Java Sound.
- Separation of networking, command processing, configuration, and playback responsibilities.
- Logging and exception handling in a small Java application.

## 2. Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Application development |
| Maven | Project build and dependency management |
| TCP sockets | Client-server communication |
| XML | Audio message-set configuration |
| Java Sound API | WAV playback |
| SLF4J and Logback | Application logging |
| IntelliJ IDEA | Development environment |

## 3. Architecture

```text
                  +----------------------+
                  |      ScanClient      |
                  +----------+-----------+
                             |
                       TCP Socket
                       Port 5000
                             |
                  +----------v-----------+
                  | AutovoicePlayerServer|
                  +----------+-----------+
                             |
                      Creates thread
                             |
                  +----------v-----------+
                  | AutoVoiceHandlerThread|
                  +----------+-----------+
                             |
                     Command dispatch
                             |
                  +----------v-----------+
                  | AutovoiceInterpreter |
                  +-----+-----------+----+
                        |           |
               Load configuration   | Request playback
                        |           |
             +----------v-----+  +--v----------------+
             | MessageSet     |  | AutovoicePlayer  |
             | Handler (XML)  |  |                  |
             +----------+-----+  +--------+---------+
                        |                 |
             +----------v-----+           |
             | MessageSet     |           |
             | Model           |           |
             +----------------+           |
                                          v
                                   Configured WAV file
```

### Component responsibilities

| Component | Responsibility |
|---|---|
| `ScanClient` | Connects to the server, sends commands, and reads responses. |
| `AutovoicePlayerServer` | Listens for TCP connections and starts a handler thread. |
| `AutoVoiceHandlerThread` | Reads client commands, invokes the interpreter, and sends responses. |
| `AutovoiceInterpreter` | Interprets commands and coordinates configuration loading and playback. |
| `AutovoiceMessageSetHandler` | Parses the XML file and loads the requested message set. |
| `AutovoiceMessageSet` | Represents the loaded message-set configuration. |
| `AutovoicePlayer` | Opens and plays the configured WAV file. |

## 4. Workflow

### Step 1 — Establish connection

`ScanClient` connects to `AutovoicePlayerServer` using a TCP socket on port `5000`.

The server accepts the connection and starts an `AutoVoiceHandlerThread` to handle communication with that client.

### Step 2 — Load the message set

The client sends:

```text
OP_AUTOVOICE_LOAD
```

The handler passes the command to `AutovoiceInterpreter`.

The interpreter requests the `MRI` / `START_SCAN` message set from `AutovoiceMessageSetHandler`, which reads the XML configuration and returns an `AutovoiceMessageSet` object.

### Step 3 — Play pre-scan audio

The client sends:

```text
OP_AUTOVOICE_PLAY_PRE
```

The interpreter retrieves the configured pre-scan WAV path and invokes `AutovoicePlayer`.

After the playback method completes successfully, the interpreter returns a success response to the handler, which sends it back to the client.

### Step 4 — Play post-scan audio

The client sends:

```text
OP_AUTOVOICE_PLAY_POST
```

The same process is followed using the configured post-scan WAV file.

### Communication flow

```text
ScanClient
    |
    | OP_AUTOVOICE_LOAD
    v
Handler Thread
    |
    v
AutovoiceInterpreter
    |
    v
XML MessageSetHandler
    |
    v
Loaded MessageSet
    |
    | OP_AUTOVOICE_PLAY_PRE
    v
AutovoicePlayer
    |
    v
WAV Playback
    |
    v
Response to Client
```

The client reuses the same TCP connection for multiple commands in the current implementation.

## 5. Project Structure

```text
AudioWire/
├── pom.xml
├── README.md
├── config/
│   └── AutovoiceMessageSets.xml
├── audio/
│   ├── pre.wav
│   └── post.wav
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── tdm/
    │               └── audiowire/
    │                   ├── ScanClient.java
    │                   ├── AutovoicePlayerServer.java
    │                   ├── AutoVoiceHandlerThread.java
    │                   ├── AutovoiceCommand.java
    │                   ├── AutovoiceInterpreter.java
    │                   ├── AutovoiceMessageSet.java
    │                   ├── AutovoiceMessageSetHandler.java
    │                   └── AutovoicePlayer.java
    └── test/
        └── java/
```

The tree represents the intended layout of the POC; adjust filenames if your local project differs.

## 6. XML Configuration

Example configuration:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<AutovoiceMessageSets>
    <MessageSet group="MRI" key="START_SCAN">
        <preWav>audio/pre.wav</preWav>
        <postWav>audio/post.wav</postWav>
        <speed>1.0</speed>
    </MessageSet>
</AutovoiceMessageSets>
```

| Field | Description |
|---|---|
| `group` | Logical group for the message set |
| `key` | Identifier of the message set |
| `preWav` | Path to the pre-scan audio file |
| `postWav` | Path to the post-scan audio file |
| `speed` | Configured playback-speed value |

The XML format above is a proposed schema for this POC, not a verified GE Healthcare configuration format.

The current player reads the WAV paths from the loaded configuration. Although `speed` is loaded, playback-speed adjustment has not been implemented.

## 7. Supported Commands

| Command | Purpose | Expected successful response |
|---|---|---|
| `PING` | Connectivity check | `PONG` |
| `OP_AUTOVOICE_LOAD` | Load the message set | `OP_AUTOVOICE_LOAD_RESP:SUCCESS` |
| `OP_AUTOVOICE_PLAY_PRE` | Play pre-scan audio | `OP_AUTOVOICE_PLAY_PRE_RESP:SUCCESS` |
| `OP_AUTOVOICE_PLAY_POST` | Play post-scan audio | `OP_AUTOVOICE_PLAY_POST_RESP:SUCCESS` |

Load and playback operations return failure responses when the corresponding operation fails.

Commands such as `UNLOAD`, `WATCHDOG`, and `EXIT` are not considered implemented features unless their handling logic has been added and tested.

## 8. Prerequisites

Install the following:

- JDK 17.
- Maven, or IntelliJ IDEA's bundled Maven support.
- IntelliJ IDEA or another Java IDE.
- A working audio output device.
- Valid WAV files at the configured paths.

## 9. Build and Run

Run the commands from the project root.

### Build the project

```bash
mvn clean package
```

### Start the server

Run `AutovoicePlayerServer.java` from IntelliJ IDEA.

Wait until the server has started and is listening on port `5000`.

### Start the client

Run `ScanClient.java` in a separate run configuration.

The client should connect to the server, send the load command, play the pre-scan audio, play the post-scan audio, and print the responses.

**Run order:** start the server first, then start the client.

Ensure that `config/AutovoiceMessageSets.xml` and the `audio/` directory are available relative to the process working directory.

## 10. Expected Output

Example client log:

```text
Scan Client starting...
Connected to Autovoice Player Server
Sending command: OP_AUTOVOICE_LOAD
Received response: OP_AUTOVOICE_LOAD_RESP:SUCCESS
Sending command: OP_AUTOVOICE_PLAY_PRE
Received response: OP_AUTOVOICE_PLAY_PRE_RESP:SUCCESS
Sending command: OP_AUTOVOICE_PLAY_POST
Received response: OP_AUTOVOICE_PLAY_POST_RESP:SUCCESS
All commands processed.
```

Exact timestamps and logging formats may differ.

A success response means the operation completed without an exception according to the current implementation. It does not independently confirm that a listener heard the audio.

## 11. Key Engineering Concepts

This POC provides hands-on experience with:

- TCP sockets and stream-based communication.
- Java threads and per-connection command processing.
- Separation of concerns and component responsibilities.
- Command dispatch and request-response handling.
- XML parsing and configuration-driven behavior.
- Audio file handling and playback lifecycle.
- Logging, exception handling, and debugging.

## 12. Current Limitations

This is a learning prototype, not production medical-device software.

The current implementation does not provide:

- Integration with an actual MRI scanner or scan-sequence controller.
- A verified GE Healthcare communication protocol.
- Native audio processing through JNI.
- ALSA-specific `SourceDataLine` integration.
- Playback-speed adjustment or audio stretching.
- Timer-driven post-message timeout handling.
- A complete unload, watchdog, or exit lifecycle.
- Production-grade protocol validation, security, recovery, or safety verification.

These are potential future engineering topics, not completed capabilities.

## 13. Potential Future Enhancements

1. Implement and test unload and lifecycle commands.
2. Add automated unit and integration tests.
3. Validate malformed commands and missing configuration files.
4. Improve socket timeout and disconnection handling.
5. Add configurable audio playback speed.
6. Explore `SourceDataLine` and native audio integration.
7. Add explicit audio cancellation and resource-management behavior.
8. Document protocol message formats and error responses.

## 14. Conclusion

AudioWire demonstrates a basic command-driven audio workflow using Java networking, XML configuration, multithreading, and WAV playback.

The project is intended to strengthen understanding of software architecture and communication patterns that can be relevant when studying audio subsystems in imaging workflows.

**Project type:** Educational POC  
**Language:** Java 17  
**Build tool:** Maven  
**Status:** Core TCP, XML-loading, and pre/post audio-playback flow demonstrated


## Future scope: Explore integration with TDM Electronics medical-device systems.
