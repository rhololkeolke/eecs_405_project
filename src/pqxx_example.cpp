
#include <iostream>
#include <pqxx/pqxx>

int main(int, char *argv[])
{
    pqxx::connection c("dbname=eecs405 user=eecs405 host=localhost port=9999");
    pqxx::work txn(c);

    pqxx::result r = txn.exec(
            "SELECT string "
                    "FROM imdbnames");

    if (r.size() != 1)
    {
        std::cerr
        << "Expected 1 string "
        << "but found " << r.size() << std::endl;
        return 1;
    }

    std::string value = r[0][0].as<std::string>();
    std::cout << "String has value " << value << std::endl;

    txn.commit();
}
