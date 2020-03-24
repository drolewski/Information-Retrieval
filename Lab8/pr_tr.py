import numpy as np

L1  = [0, 1, 1, 0, 0, 0, 0, 0, 0, 0]
# L1  = [0, 1, 1, 0, 1, 0, 0, 0, 0, 0]
L2  = [1, 0, 0, 1, 0, 0, 0, 0, 0, 0]
L3  = [0, 1, 0, 0, 0, 0, 0, 0, 0, 0]
# L3  = [0, 1, 0, 0, 0, 0, 1, 0, 0, 0]
L4  = [0, 1, 1, 0, 0, 0, 0, 0, 0, 0]
L5  = [0, 0, 0, 0, 0, 1, 1, 0, 0, 0]
L6  = [0, 0, 0, 0, 0, 0, 1, 1, 0, 0]
L7  = [0, 0, 0, 0, 1, 1, 1, 1, 1, 1]
L8  = [0, 0, 0, 0, 0, 0, 1, 0, 1, 0]
L9  = [0, 0, 0, 0, 0, 0, 1, 0, 0, 1]
L10 = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

L = np.array([L1, L2, L3, L4, L5, L6, L7, L8, L9, L10])

ITERATIONS = 100

def getM(L):
    M = np.zeros([10, 10], dtype=float)
    # number of outgoing links
    c = np.zeros([10], dtype=int)

    ## TODO 1 compute the stochastic matrix M
    for i in range(0, 10):
        c[i] = sum(L[i])

    for i in range(0, 10):
        for j in range(0, 10):
            if L[j][i] == 0:
                M[i][j] = 0
            else:
                M[i][j] = 1.0/c[j]
    return M

print("Matrix L (indices)")
print(L)

M = getM(L)

print("Matrix M (stochastic matrix)")
print(M)

### TODO 2: compute pagerank with damping factor q = 0.15
### Then, sort and print: (page index (first index = 1 add +1) : pagerank)
### (use regular array + sort method + lambda function)
print("PAGERANK")

q = 0.15

pr = np.zeros([10], dtype=float)
pr = [1.0/len(L1) for i in range(len(L1))]

for i in range(ITERATIONS):
    pr = q+(1-q)*np.dot(M,pr)

#normalise
pr = pr/sum(pr)

index_sort = sorted([(i + 1, pr[i]) for i in range(len(pr))], key=lambda x: x[1], reverse=True)
print(index_sort)

### TODO 3: compute trustrank with damping factor q = 0.15
### Documents that are good = 1, 2 (indexes = 0, 1)
### Then, sort and print: (page index (first index = 1, add +1) : trustrank)
### (use regular array + sort method + lambda function)
print("TRUSTRANK (DOCUMENTS 1 AND 2 ARE GOOD)")

q = 0.15

d = np.zeros([10], dtype=float)
d[0] = 1
d[1] = 1

tr = [v/sum(d) for v in d]

sq = np.copy(tr)

for i in range(0, ITERATIONS):
    tr = np.copy(q*sq+(1-q)*M@tr)

#normalise
tr = tr/sum(tr)

index_sort2 = sorted([(i + 1, tr[i]) for i in range(len(tr))], key=lambda x: x[1], reverse=True)
print(index_sort2)
### TODO 4: Repeat TODO 3 but remove the connections 3->7 and 1->5 (indexes: 2->6, 0->4)
### before computing trustrank