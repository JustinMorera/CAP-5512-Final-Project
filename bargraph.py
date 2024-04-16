import pandas as pd
import matplotlib.pyplot as plt

data = pd.read_csv('allele.csv', header=None, names=['gene', 'allele', 'frequency'])

data['gene'] = data['gene'].astype(str)
data['allele'] = data['allele'].astype(str)

data['Label'] = data['gene'] + ', ' + data['allele']

plt.figure(figsize=(10, 8))
plt.bar(data['Label'], data['frequency'], color='blue')
plt.xlabel('Gene, Allele')
plt.ylabel('Frequency')
plt.title('Frequency of Alleles')
plt.xticks(rotation=45)
plt.tight_layout()
plt.savefig('gene_allele_frequency_chart.png', format='png', dpi=300)

