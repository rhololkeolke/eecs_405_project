#include "gtest/gtest.h"
#include "gmock/gmock.h"

using ::testing::Return;

class Foo {
public:
    virtual bool baz() = 0;
};

class MockFoo : public Foo {
public:
    MOCK_METHOD0(baz, bool());
};

TEST(SampleTest, AssertionTrue) {

    MockFoo foo;
    EXPECT_CALL(foo, baz()).Times(1).WillOnce(Return(true));

    EXPECT_TRUE(foo.baz());

    ASSERT_EQ(1, 1);
}

int main(int argc, char** argv) {
    // The following line must be executed to initialize Google Mock
    // (and Google Test) before running the tests.
    ::testing::InitGoogleMock(&argc, argv);
    return RUN_ALL_TESTS();
}