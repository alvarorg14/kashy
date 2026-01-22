# Kashy

[![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![GitHub Release](https://img.shields.io/github/v/release/alvarorg14/kashy?include_prereleases&sort=semver)](https://github.com/alvarorg14/kashy/releases)
[![GitHub Issues](https://img.shields.io/github/issues/alvarorg14/kashy)](https://github.com/alvarorg14/kashy/issues)
[![Renovate](https://img.shields.io/badge/renovate-enabled-brightgreen?logo=renovatebot)](https://github.com/renovatebot/renovate)
[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg)](CODE_OF_CONDUCT.md)

Kashy is a personal finance management application powered by AI, designed to help you take control of your financial life.

## Features

- **Multi-module Architecture**: Built with Maven multi-module structure for scalability and maintainability
- **Spring Boot**: Modern Java framework for building robust applications
- **API-First Design**: RESTful API module for flexible integrations

## Prerequisites

- Java 25 or higher
- Maven 3.6+ 
- Git

## Installation

1. Clone the repository:
```bash
git clone https://github.com/alvarorg14/kashy.git
cd kashy
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
cd kashy-api
mvn spring-boot:run
```

## Project Structure

```
kashy/
├── kashy-api/          # API module
└── pom.xml             # Parent POM
```

## Development

See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on how to contribute to this project.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE.md](LICENSE.md) file for details.

## Contributing

Contributions are welcome! Please read our [Contributing Guide](CONTRIBUTING.md) and [Code of Conduct](CODE_OF_CONDUCT.md) before submitting pull requests.

## Security

Please see [SECURITY.md](SECURITY.md) for information about security vulnerabilities and how to report them.

## Author

**alvarorg14**

- GitHub: [@alvarorg14](https://github.com/alvarorg14)
