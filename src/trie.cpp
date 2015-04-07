//
// Created by Devin Schwab on 4/7/15.
//

#include "trie.h"
#include <stdexcept>

namespace EECS405 {
    namespace Trie {

        Node::Node(int freq, bool is_leaf) : is_leaf_(is_leaf) {
            if(freq < 0) {
                throw std::invalid_argument("You cannot set frequency less than 0");
            }
            frequency_ = freq;
        }

        bool Node::IsLeaf() const {
            return is_leaf_;
        }

        int Node::frequency() const {
            return frequency_;
        }

        void Node::set_frequency(int f) {
            if(f < 0) {
                throw std::invalid_argument("You cannot set frequency less than 0");
            }
            frequency_ = f;
        }

        void Node::increment_frequency() {
            frequency_++;
        }

        std::shared_ptr<Node> Node::GetChild(char child_key) {
            ChildMap::const_iterator child;
            if((child = children_.find(child_key)) != children_.end()) {
                return child->second;
            }
            return nullptr;
        }

        void Node::RemoveChild(char child_key) {
            ChildMap::iterator child;
            if((child = children_.find(child_key)) != children_.end()) {
                children_.erase(child);
            }
        }

        std::shared_ptr<Node> Node::AddChild(char child_key, int freq) {
            if (is_leaf_) {
                throw std::runtime_error("Cannot add children to leaf nodes");
            }

            ChildMap::iterator child;
            if((child = children_.find(child_key)) != children_.end()) {
                return child->second;
            }

            std::shared_ptr<Node> new_child(new Node(freq, false));
            children_[child_key] = new_child;

            return new_child;
        }

        unsigned long Node::num_children() {
            return children_.size();
        }

        bool Node::LeafExists() {
            return leaf_.get() != nullptr;
        }

        std::shared_ptr<Node> Node::CreateLeaf(int freq) {
            if (leaf_ != nullptr) {
                throw std::logic_error("Leaf already exists!");
            }
            leaf_ = std::shared_ptr<Node>(new Node(freq, true));
            return leaf_;
        }

        std::shared_ptr<Node> Node::GetLeaf() {
            return leaf_;
        }
    }
}