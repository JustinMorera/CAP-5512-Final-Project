import pandas as pd
import matplotlib.pyplot as plt

def get_dividing_number(column, df):
    max_abs_value = df[column].abs().max()
    if max_abs_value > 100000:
        return 10000
    if max_abs_value > 10000:
        return 1000
    elif max_abs_value > 1000:
        return 100
    elif max_abs_value > 100:
        return 10
    else:
        return 1

def plot_graph():
    df = pd.read_csv('fitnessdata.csv', header=None, names=['Generation', 'Average Fit', 'Best Fit', 'Population', 'Cumulative Population', 'Event'])

    dividing_number_best_fit = get_dividing_number('Best Fit', df)
    dividing_number_average_fit = get_dividing_number('Average Fit', df)
    dividing_number_population = get_dividing_number('Population', df)
    dividing_number_cum_population = get_dividing_number('Cumulative Population', df)

    #df['Population Derivative'] = df['Population'].diff() / dividing_number_population

    fig, ax = plt.subplots()
    ax.plot(df['Generation'], -df['Best Fit'] / dividing_number_best_fit, 
            label=f'Best Fit (divided by {dividing_number_best_fit})', marker='o')
    ax.plot(df['Generation'], df['Average Fit'] / dividing_number_average_fit, 
            label=f'Average Fit (divided by {dividing_number_average_fit})', marker='o')
    ax.plot(df['Generation'], df['Population'] / dividing_number_population, 
            label=f'Population (divided by {dividing_number_population})', marker='o')
    ax.plot(df['Generation'], df['Cumulative Population'] / dividing_number_cum_population, 
            label=f'Cumulative Population (divided by {dividing_number_cum_population})', marker='o')
    #ax.plot(df['Generation'], df['Population Derivative'],
    #        label='Population Derivative', linestyle='--', color='green')

    event_indices = df[df['Event'] == 1]['Generation']
    for gen in event_indices:
        ax.axvline(x=gen, color='red', linestyle='--', alpha=0.7)

    ax.set_xlabel('Generation')
    ax.set_ylabel('Fitness and Population')
    ax.set_title('Fitness Evolution Over Generations')

    ax.legend()

    plt.savefig('fitness_evolution.png', dpi=300)
    plt.close()

plot_graph()

