cmake_minimum_required(VERSION 3.1)
project(eecs_405_project)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_SOURCE_DIR}/cmake/Modules/")

find_package(Pqxx REQUIRED)

# This breaks Clion even though cmake from the command
# line is fine with it. Not sure why.
#include_directories(${Pqxx_INCLUDE_DIRS})
#message(${Pqxx_INCLUDE_DIRS})

add_executable(pqxx_example src/pqxx_example.cpp)
target_link_libraries(pqxx_example ${Pqxx_LIBRARIES})