import pandas as pd
import matplotlib.pyplot as plt
import imageio
import os

data = pd.read_csv('allele.csv', header=None, names=['generation', 'gene', 'allele', 'frequency'])

generations = data['generation'].unique()

if not os.path.exists('charts'):
    os.makedirs('charts')

images = []

for generation in generations:
    gen_data = data[data['generation'] == generation]
    
    pivot_data = gen_data.pivot_table(index='gene', columns='allele', values='frequency', fill_value=0)

    plt.figure(figsize=(14, 8))
    pivot_data.plot(kind='bar', stacked=True, colormap='viridis', edgecolor='black')
    
    plt.xlabel('Gene')
    plt.ylabel('Frequency')
    plt.title(f'Frequency of Alleles per Gene - Generation {generation}')
    plt.xticks(rotation=0)
    plt.legend(title='Allele', bbox_to_anchor=(1.05, 1), loc='upper left') 
    plt.tight_layout()
    
    file_path = f'charts/generation_{generation}.png'
    plt.savefig(file_path)
    plt.close()
    
    images.append(imageio.imread(file_path))

gif_path = 'gene_allele_frequency_animation.gif'
imageio.mimsave(gif_path, images, fps=1, loop=0)

print("GIF created at:", gif_path)

