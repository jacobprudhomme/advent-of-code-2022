plugins {
    kotlin("jvm") version "1.7.22"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs(
                "src/day01",
                "src/day02",
                "src/day03",
                "src/day04",
                "src/day05",
                "src/day06",
                "src/day07",
                "src/day08",
                "src/day09",
                "src/day10",
                "src/day11",
                "src/day12",
                "src/day13",
                "src/day14",
                "src/day15",
                "src/day16",
                "src/day17",
                "src/day18",
                "src/day19",
                "src/day20",
                "src/day21",
                "src/day22",
                "src/day23",
                "src/day24",
                "src/day25"
            )
        }
    }

    wrapper {
        gradleVersion = "7.6"
    }
}
