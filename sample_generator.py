from datetime import datetime
from random import choices, randint
from string import ascii_letters, digits, punctuation
from sys import argv

symbols = ascii_letters + digits + punctuation

def generate_row(max_len):
    return ''.join(choices(symbols, k=randint(1, max_len)))

error_msg = "Enter arguments N - number of rows in sample file and L - maximum length of a row"
if len(argv) != 3:
    raise Exception(error_msg)
try:
    n = int(argv[1])
    m = int(argv[2])
except ValueError:
    raise Exception(error_msg)

with open('sample_' + datetime.now().strftime("%Y-%m-%d-%H-%M-%S") + '.txt', 'w') as fs:
    for i in range(n - 1):
        fs.write(generate_row(m) + '\n')
    ## writing last row without empty next line
    fs.write(generate_row(m))
