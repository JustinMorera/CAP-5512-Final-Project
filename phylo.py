import pandas as pd
import numpy as np
import matplotlib as plt
from Bio import Phylo
from sklearn.neighbors import NearestNeighbors
from skbio.tree import nj
from skbio import DistanceMatrix

def hamming_distance(seq1, seq2):
    if len(seq1) != len(seq2):
        raise ValueError("Sequences must be the same length")
    
    # Count mismatches
    distance = sum(c1 != c2 for c1, c2 in zip(seq1, seq2))

    return distance

# Main parameters
sample_size = 5

csv_path = "phylo.csv"
df = pd.read_csv(csv_path, dtype={"Genome": str})
df['AvgFitness'] = pd.to_numeric(df['AvgFitness'], errors='coerce')

# Drop individuals born with a lethal allele and remove duplicates
df = df[df['AvgFitness'] >= 0]
df.drop_duplicates(subset=["Genome"], keep="first", inplace=True)

# Extract weights and data for sampling
weights = df["AvgFitness"].to_numpy()
weights = weights / np.sum(weights)
data = df.drop("AvgFitness", axis=1)

# Weighted random sampling
sampled_indices = np.random.choice(data.index, size=sample_size, replace=False, p=weights)
sampled_data = df.loc[sampled_indices]

# Create an empty similarity matrix
similarity_matrix = [[0 for _ in range(len(sampled_data))] for _ in range(len(sampled_data))]

# Iterate through all pairs of individuals
for i in range(len(sampled_data)):
  for j in range(i + 1, len(sampled_data)):
    individual1_genome = sampled_data["Genome"].iloc[i]
    individual2_genome = sampled_data["Genome"].iloc[j]
    distance = hamming_distance(individual1_genome, individual2_genome)
    similarity_matrix[i][j] = distance
    similarity_matrix[j][i] = distance  # Fill the other half for symmetry

# Build the tree
dm = DistanceMatrix(similarity_matrix, list(sampled_data["Genome"]))
tree = nj(dm)

print(tree.ascii_art())
