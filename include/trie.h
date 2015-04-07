//
// Created by Devin Schwab on 4/7/15.
//

#ifndef EECS_405_PROJECT_TRIE_H
#define EECS_405_PROJECT_TRIE_H

namespace EECS405 {

    namespace Trie {

        class Node {
        public:
            Node() : freq(0) {};

            Node(int f);

            int getFreq() const;

            void setFreq(int f);

            void incrFreq();

        private:
            int freq;
        };
    }
}

#endif //EECS_405_PROJECT_TRIE_H
