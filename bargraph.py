import pandas as pd
import matplotlib.pyplot as plt

data = pd.read_csv('allele.csv', header=None, names=['gene', 'allele', 'frequency'])

pivot_data = data.pivot_table(index='gene', columns='allele', values='frequency', fill_value=0)

plt.figure(figsize=(14, 8))

pivot_data.plot(kind='bar', stacked=True, colormap='viridis', edgecolor='black')

plt.xlabel('Gene')
plt.ylabel('Frequency')
plt.title('Stacked Frequency of Alleles per Gene')
plt.xticks(rotation=0)
plt.legend(title='Allele', bbox_to_anchor=(1.05, 1), loc='upper left')
plt.tight_layout()

plt.savefig('bar_graph.png', format='png', dpi=300)
plt.show()

