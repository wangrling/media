
#include <thread>
#include <string>

void openDocumentAndDisplayGui(std::string const& filename) {

}

bool doneEditing() {
    return true;
}

enum CommandType {
    OPEN_NEW_DOCUMENT
};

struct UserCommand {
    CommandType type;

    UserCommand(): type(OPEN_NEW_DOCUMENT) {

    };
};

UserCommand getUserInput() {
    return UserCommand();
}

std::string getFilenameFromUser() {
    return "foo.doc";
}

void processUserInput(UserCommand const& cmd) {

}

void editDocument(std::string const& filename) {
    openDocumentAndDisplayGui(filename);
    while (!doneEditing()) {
        UserCommand cmd = getUserInput();
        if (cmd.type == OPEN_NEW_DOCUMENT) {
            std::string const newName = getFilenameFromUser();
            std::thread t(editDocument, newName);
            t.detach();
        } else {
            processUserInput(cmd);
        }
    }
}

int main() {
    editDocument("bar.doc");
}