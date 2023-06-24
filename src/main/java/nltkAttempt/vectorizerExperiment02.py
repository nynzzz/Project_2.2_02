import nltk
from nltk.corpus import stopwords
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np

nltk.download('punkt')
nltk.download('stopwords')

# Prepare your data
questions = [
    "What is your name?", "How are you?", "What is the weather today?",
    "What is the weather tomorrow in Maastricht?", "How is the weather in Berlin?",
    "My mother is in New York today. What is the weather?", "How is the weather in New York today?"
]
answers = [
    "My name is ChatBot.", "I'm doing well, thank you.", "The weather is rainy.",
    "The weather tomorrow in Maastricht will be sunny", "It is rainy",
    "It is stormy in newYork", "It is stormy in newYork"
]

# Preprocess your data
stop_words = set(stopwords.words('english'))


def preprocess_text(text):
    tokens = nltk.word_tokenize(text.lower())
    tokens = [token for token in tokens if token.isalpha() and token not in stop_words]
    return ' '.join(tokens)


preprocessed_questions = [preprocess_text(question) for question in questions]

# Define vectorizers to compare
vectorizers = [
    ('TF-IDF', True),
    ('CountVectorizer', True),
    ('No Vectorizer', False)
]

# Iterate over vectorizers and evaluate performance
for vectorizer_name, use_vectorizer in vectorizers:
    print(f"Evaluating {vectorizer_name}:")

    if use_vectorizer:
        vectorizer = TfidfVectorizer() if vectorizer_name == 'TF-IDF' else CountVectorizer()
        vectorized_data = vectorizer.fit_transform(preprocessed_questions)
        embeddings_matrix = vectorized_data.toarray()
    else:
        embeddings_matrix = None

    user_question = "How is the weather in maastricht"  # Adjust the question with a typo or missing part

    # Retrieve answers based on similarity
    def get_answer(user_question):
        preprocessed_question = preprocess_text(user_question)

        if use_vectorizer:
            tfidf_vector = vectorizer.transform([preprocessed_question])
            similarities = cosine_similarity(tfidf_vector, embeddings_matrix)
        else:
            similarities = np.zeros(len(questions))  # No vectorizer, set similarities to 0

        most_similar_index = similarities.argmax()
        return similarities, answers[most_similar_index]


    similarities, response = get_answer(user_question)
    print(f"ChatBot response: {response}")
    print("Similarity scores:")

    if use_vectorizer:
        for i, sim in enumerate(similarities):
            print(f"Question: {questions[i]}")
            print(f"Similarity: {sim}")
    else:
        print(f"Question: {user_question}")
        print(f"Similarity: {similarities}")
