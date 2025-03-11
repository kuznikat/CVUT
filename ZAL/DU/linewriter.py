STATICKY_TEXT = "This is my static text which must be added to file. It is very long text and I do not know what they want to do with this terrible text. "
FILE_NAME = "My_Task"

def writeTextToFile(param):
    result = STATICKY_TEXT + str(param)
    with open(FILE_NAME, "w") as my_file :
        my_file.write(result)
    return FILE_NAME

