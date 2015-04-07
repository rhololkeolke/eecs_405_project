//
// Created by Devin Schwab on 4/7/15.
//

#ifndef EECS_405_PROJECT_TRIE_H
#define EECS_405_PROJECT_TRIE_H

#include <unordered_map>
#include <memory>

namespace EECS405 {

    namespace Trie {

        class Trie;

        class Node {
            friend class Trie;
        public:
            typedef std::unordered_map<char, std::shared_ptr<Node> > ChildMap;

            Node(int freq=0, bool is_leaf=false);

            bool IsLeaf() const;

            int frequency() const;
            void set_frequency(int f);
            void increment_frequency();

            bool LeafExists();
            std::shared_ptr<Node> GetLeaf();
            std::shared_ptr<Node> CreateLeaf(int freq=0);


            std::shared_ptr<Node> GetChild(char child_key);
            void RemoveChild(char child_key);
            std::shared_ptr<Node> AddChild(char child_key, int freq=0);
            unsigned long num_children();
        private:
            int frequency_;

            const bool is_leaf_;
            std::shared_ptr<Node> leaf_;

            ChildMap children_;
        };

        class Trie {
        public:
            Trie() {

            }

        private:

        };
    }
}

#endif //EECS_405_PROJECT_TRIE_H
