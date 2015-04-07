#include "gtest/gtest.h"
#include "trie.h"
#include <stdexcept>

using namespace EECS405;

TEST(TrieNode, ConstructNodeDefaultArgs) {
    Trie::Node n;

    ASSERT_EQ(0, n.getFreq());
}

TEST(TrieNode, ConstructNodeSpecifyFreq) {
    Trie::Node n(10);

    ASSERT_EQ(10, n.getFreq());
}

TEST(TrieNode, ConstructNodeFreqBelow0) {
    EXPECT_THROW(Trie::Node n(-1), std::invalid_argument);
}

TEST(TrieNode, getFreq) {
    Trie::Node n;

    ASSERT_EQ(0, n.getFreq());
}

TEST(TrieNode, setFreqBelow0) {
    Trie::Node n;

    EXPECT_THROW(n.setFreq(-10), std::invalid_argument);
}

TEST(TrieNode, setFreq) {
    Trie::Node n;

    ASSERT_NE(10, n.getFreq());
    n.setFreq(10);
    ASSERT_EQ(10, n.getFreq());
}

TEST(TrieNode, incrFreq) {
    Trie::Node n;

    ASSERT_EQ(0, n.getFreq());
    n.incrFreq();
    ASSERT_EQ(1, n.getFreq());
}

int main(int argc, char** argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}

