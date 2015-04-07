//
// Created by Devin Schwab on 4/7/15.
//

#include "trie.h"
#include <stdexcept>

namespace EECS405 {
    namespace Trie {

        Node::Node(std::shared_ptr<Node> parent, int f) {
            if(f < 0) {
                throw std::invalid_argument("You cannot set frequency less than 0");
            }
            frequency_ = f;

            parent_ = parent;
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
            // TODO
            return std::shared_ptr<Node>();
        }

        void Node::RemoveChild(char child_key) {
            // TODO
        }

        std::shared_ptr<Node> Node::AddChild(char child_key, int freq) {
            // TODO

            std::unordered_map<char, std::shared_ptr<Node> >::iterator child;
            if((child = children_.find(child_key)) != children_.end()) {
                return child->second;
            }

            std::shared_ptr<Node> new_child(new Node(std::shared_ptr<Node>(this), freq));
            children_[child_key] = new_child;

            return new_child;
        }

        unsigned long Node::num_children() {
            return children_.size();
        }

        std::shared_ptr<Node> Node::GetParent() {
            return parent_.lock();
        }
    }
}