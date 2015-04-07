//
// Created by Devin Schwab on 4/7/15.
//

#include "trie.h"
#include <stdexcept>

namespace EECS405 {
    namespace Trie {
        Node::Node(int f) {
            if(f < 0) {
                throw std::invalid_argument("You cannot set frequency less than 0");
            }
            freq = f;
        }

        int Node::getFreq() const {
            return freq;
        }

        void Node::setFreq(int f) {
            if(f < 0) {
                throw std::invalid_argument("You cannot set frequency less than 0");
            }
            freq = f;
        }

        void Node::incrFreq() {
            freq++;
        }
    }
}