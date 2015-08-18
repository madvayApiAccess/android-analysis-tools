# apat: android-analysis-tools
Tools to analyze performance of Android applications.

## Running from a distribution
Run the `bin/apat` script per the Usage section.

## Usage
<!-- The content between the ```hashes``` will also be displayed
     as the usage notice by the Java binary. -->
```
android-analysis-tools - https://madvay.com/source/apat
Tools to analyze performance of Android applications.

Usage:
apat <command> <options>

Available commands:
 help               - Prints this usage message.
 version            - Prints version and copyright notice.
 license            - Prints the full LICENSE file.
 allocs             - Allocation tracking analysis:
    parse <file>        - Parses a DDMS .alloc file
```

## Building from source
1.  Clone the repository: `git clone https://github.com/madvay/android-analysis-tools.git`
2.  Build and run from the local repository: `./apat [args]`

## License
See the [LICENSE](LICENSE) and the [NOTICE](NOTICE) (per Section 4 d of the License) files.
