#include "gtest/gtest.h"
#include "trie.h"
#include <stdexcept>

using namespace EECS405;

TEST(TrieNode, ConstructNodeDefaultArgs) {
    Trie::Node n;

    ASSERT_EQ(0, n.frequency());
    ASSERT_EQ(nullptr, n.GetParent());
}

TEST(TrieNode, ConstructNodeSpecifyFreq) {
    Trie::Node n(10);

    ASSERT_EQ(10, n.frequency());
    ASSERT_EQ(nullptr, n.GetParent());
}

TEST(TrieNode, ConstructNodeSpecifyParent) {
    std::shared_ptr<Trie::Node> parent(new Trie::Node());
    Trie::Node child(parent);

    EXPECT_EQ(parent, child.GetParent());
    EXPECT_EQ(0, child.frequency());
}

TEST(TrieNode, FullConstructor) {
    std::shared_ptr<Trie::Node> parent(new Trie::Node());
    Trie::Node child(parent, 10);

    EXPECT_EQ(parent, child.GetParent());
    EXPECT_EQ(10, child.frequency());
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
    ASSERT_EQ(&n, childA->GetParent().get());
    ASSERT_EQ(10, n.frequency());
    ASSERT_EQ(1, n.num_children());

    std::shared_ptr<Trie::Node> retrievedChild = n.GetChild('a');
    ASSERT_EQ(childA, retrievedChild);
}

int main(int argc, char** argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}

