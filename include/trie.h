//
// Created by Devin Schwab on 4/7/15.
//

#ifndef EECS_405_PROJECT_TRIE_H
#define EECS_405_PROJECT_TRIE_H

#include <unordered_map>
#include <memory>

namespace EECS405 {

    namespace Trie {

        class Node {
        public:
            Node() : Node(std::shared_ptr<Node>(), 0){};
            Node(int f) : Node(std::shared_ptr<Node>(), f) {};
            Node(std::shared_ptr<Node> parent) : Node(parent, 0) {};
            Node(std::shared_ptr<Node> parent, int f);

            int frequency() const;

            void set_frequency(int f);

            void increment_frequency();

            std::shared_ptr<Node> GetChild(char child_key);
            void RemoveChild(char child_key);
            std::shared_ptr<Node> AddChild(char child_key, int freq = 0);
            unsigned long num_children();

            std::shared_ptr<Node> GetParent();

        protected:

        private:

            int frequency_;

            std::unordered_map<char, std::shared_ptr<Node> > children_;
            std::weak_ptr<Node> parent_;
        };
    }
}

#endif //EECS_405_PROJECT_TRIE_H
