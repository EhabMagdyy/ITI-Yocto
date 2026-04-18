#include <stdio.h>
#include "mymath.h"

int main(int argc, char *argv[]){
    
    if(argc != 4){
        printf("Usage: %s <operation> <num1> <num2>\n", argv[0]);
        return 1;
    }

    int num1 = argv[2][0] - '0';
    int num2 = argv[3][0] - '0';

    if(argv[1][0] == '+'){
        printf("Result: %d\n", add(num1, num2));
    } else if(argv[1][0] == '-'){
        printf("Result: %d\n", sub(num1, num2));
    } else if(argv[1][0] == '*'){
        printf("Result: %d\n", mul(num1, num2));
    } else if(argv[1][0] == '/'){
        printf("Result: %d\n", div(num1, num2));
    } else {
        printf("Invalid operation. Use +, -, *, or /.\n");
        return 1;
    }

    return 0;
}