import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from Bio import Phylo
from io import StringIO
from skbio.tree import nj
from skbio import DistanceMatrix

def hamming_distance(seq1, seq2):
    if len(seq1) != len(seq2):
        raise ValueError("Sequences must be the same length")
    
    distance = sum(c1 != c2 for c1, c2 in zip(seq1, seq2))
    return distance

sample_size = 10
csv_path = "phylo.csv"
df = pd.read_csv(csv_path, dtype={"Genome": str})
df['AvgFitness'] = pd.to_numeric(df['AvgFitness'], errors='coerce')

df = df[df['AvgFitness'] >= 0]
df.drop_duplicates(subset=["Genome"], keep="first", inplace=True)

weights = df["AvgFitness"].to_numpy()
weights = weights / np.sum(weights)
data = df.drop("AvgFitness", axis=1)

sampled_indices = np.random.choice(data.index, size=sample_size, replace=False, p=weights)
sampled_data = df.loc[sampled_indices]

similarity_matrix = [[0 for _ in range(len(sampled_data))] for _ in range(len(sampled_data))]

for i in range(len(sampled_data)):
    for j in range(i + 1, len(sampled_data)):
        individual1_genome = sampled_data["Genome"].iloc[i]
        individual2_genome = sampled_data["Genome"].iloc[j]
        distance = hamming_distance(individual1_genome, individual2_genome)
        similarity_matrix[i][j] = distance
        similarity_matrix[j][i] = distance

dm = DistanceMatrix(similarity_matrix, list(sampled_data["Genome"]))
tree = nj(dm)

newick_out = StringIO()
tree.write(newick_out, format="newick")
tree_newick = newick_out.getvalue()

tree = Phylo.read(StringIO(tree_newick), 'newick')

fig = plt.figure(figsize=(10, 10), dpi=300)
axes = fig.add_subplot(1, 1, 1)
Phylo.draw(tree, axes=axes, do_show=False, label_func=lambda x: "", show_confidence=False, branch_labels=lambda x: x.branch_length)

plt.savefig('tree.png')

