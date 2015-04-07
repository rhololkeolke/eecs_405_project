#include "gtest/gtest.h"
#include "trie.h"
#include <stdexcept>

using namespace EECS405;

TEST(TrieNode, ConstructNodeDefaultArgs) {
    Trie::Node n;

    EXPECT_EQ(0, n.frequency());
    EXPECT_FALSE(n.IsLeaf());
}

TEST(TrieNode, ConstructFrequency) {
    Trie::Node n(10);

    EXPECT_EQ(10, n.frequency());
    EXPECT_FALSE(n.IsLeaf());
}

TEST(TrieNode, ConstructFull) {
    Trie::Node n(10, true);

    EXPECT_EQ(10, n.frequency());
    EXPECT_TRUE(n.IsLeaf());
}

TEST(TrieNode, ConstructNodeFreqBelow0) {
    EXPECT_THROW(Trie::Node n(-1), std::invalid_argument);
}

TEST(TrieNode, getFreq) {
    Trie::Node n;

    ASSERT_EQ(0, n.frequency());
}

TEST(TrieNode, setFreqBelow0) {
    Trie::Node n;

    EXPECT_THROW(n.set_frequency(-10), std::invalid_argument);
}

TEST(TrieNode, setFreq) {
    Trie::Node n;

    ASSERT_NE(10, n.frequency());
    n.set_frequency(10);
    ASSERT_EQ(10, n.frequency());
}

TEST(TrieNode, incrFreq) {
    Trie::Node n;

    ASSERT_EQ(0, n.frequency());
    n.increment_frequency();
    ASSERT_EQ(1, n.frequency());
}

TEST(TrieNode, AddChild) {
    Trie::Node n;

    ASSERT_EQ(0, n.num_children());

    n.AddChild('a');
    ASSERT_EQ(1, n.num_children());

    n.AddChild('b');
    ASSERT_EQ(2, n.num_children());
}

TEST(TrieNode, GetChild) {
    Trie::Node n;

    ASSERT_EQ(0, n.num_children());

    std::shared_ptr<Trie::Node> childA = n.AddChild('a', 10);
    ASSERT_EQ(10, childA->frequency());
    ASSERT_EQ(1, n.num_children());

    std::shared_ptr<Trie::Node> retrievedChild = n.GetChild('a');
    ASSERT_EQ(childA, retrievedChild);
}

TEST(TrieNode, RemoveChild) {
    Trie::Node n;

    ASSERT_EQ(0, n.num_children());
    n.AddChild('a');
    ASSERT_EQ(1, n.num_children());
    ASSERT_NE(nullptr, n.GetChild('a'));

    n.RemoveChild('a');
    ASSERT_EQ(0, n.num_children());
    ASSERT_EQ(nullptr, n.GetChild('a'));
}

TEST(TrieNode, LeafExists) {
    Trie::Node n;

    ASSERT_FALSE(n.LeafExists());
    n.CreateLeaf();
    ASSERT_EQ(0, n.num_children());
    ASSERT_TRUE(n.LeafExists());
}

TEST(TrieNode, GetLeaf) {
    Trie::Node n;

    ASSERT_FALSE(n.LeafExists());
    EXPECT_EQ(nullptr, n.GetLeaf());
    n.CreateLeaf();
    EXPECT_NE(nullptr, n.GetLeaf());
}

TEST(TrieNode, CreateLeaf) {
    Trie::Node n;

    ASSERT_FALSE(n.LeafExists());
    std::shared_ptr<Trie::Node> leaf = n.CreateLeaf(10);
    ASSERT_EQ(0, n.num_children());
    ASSERT_NE(nullptr, leaf);
    ASSERT_EQ(10, leaf->frequency());
}

TEST(TrieNode, AddChildFailsForLeaf) {
    Trie::Node n(10, true);
    ASSERT_TRUE(n.IsLeaf());
    ASSERT_THROW(n.AddChild('a'), std::runtime_error);
}

int main(int argc, char** argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}

