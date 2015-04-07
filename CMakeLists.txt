cmake_minimum_required(VERSION 3.1)
project(eecs_405_project)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11") # -lpthread")

set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_SOURCE_DIR}/cmake/Modules/")

find_package(Pqxx REQUIRED)

# This breaks Clion even though cmake from the command
# line is fine with it. Not sure why.
#include_directories(${Pqxx_INCLUDE_DIRS})
#message(${Pqxx_INCLUDE_DIRS})

include_directories(include)

add_executable(pqxx_example src/pqxx_example.cpp)
target_link_libraries(pqxx_example ${Pqxx_LIBRARIES})

add_library(trie src/trie.cpp)

#########
# Tests #
#########
enable_testing()
add_subdirectory(gmock-1.7.0)
include_directories(${gmock_SOURCE_DIR}/include ${gmock_SOURCE_DIR}/gtest/include)

add_executable(trie_tests test/trie_tests.cpp)
target_link_libraries(trie_tests pthread gmock trie)