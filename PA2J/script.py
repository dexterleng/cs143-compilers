import os
import subprocess
import itertools

def repeat_to_length(string_to_expand, length):
    return (string_to_expand * (int(length/len(string_to_expand))+1))[:length]

output_input_tup = list(
    sorted(map(lambda x: (x, x[:-4]), filter(lambda x: x.endswith(".out"), os.listdir("grading/"))))
)
count = 0
for x in output_input_tup:
    (out, inp) = x

    result = subprocess.run(['./lexer', "grading/" + inp], stdout=subprocess.PIPE)
    stdout = result.stdout.decode("utf-8").split("\n", 1)[1]

    expected_result = subprocess.run(['cat', "grading/" + out], stdout=subprocess.PIPE)
    expected_stdout = expected_result.stdout.decode("utf-8").split("\n", 1)[1]

    match = stdout == expected_stdout
    if not match:
        print(out + "\n")

        l0 = stdout.split("\n")

        # Read the second file
        l1 = expected_stdout.split("\n")

        ls = list(itertools.zip_longest(l0, l1))
        max_from_l0 = len(max(l0, key=lambda x: len(x)))
        additional_padding_length = 20
        for e in ls:
            (a, b) = e
            if a is None:
                a = "None"
            if b is None:
                b = "None"
            if (a != b):
                print("\nNon-match:")
            padding_length = max(max_from_l0 - len(a), 0) + additional_padding_length
            padding = repeat_to_length(" ", padding_length)
            print(a + padding + b)

        print("\n\n\n")
    else:
        count += 1
print("Grade: " + str(count) + " / " + str(len(output_input_tup)))

    

