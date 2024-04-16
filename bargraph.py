import pandas as pd
import matplotlib.pyplot as plt

data = pd.read_csv('allele.csv')

data['Label'] = data['gene'] + ', ' + data['allele']

plt.figure(figsize=(10, 8))
plt.bar(data['Label'], data['frequency'], color='blue')
plt.xlabel('Gene, Allele')
plt.ylabel('Frequency')
plt.title('Frequency of Gene-Allele Combinations')
plt.xticks(rotation=45)
plt.tight_layout()
plt.savefig('gene_allele_frequency_chart.png', format='png', dpi=300)
