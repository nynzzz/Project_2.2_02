import nltk
from nltk.corpus import stopwords
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
nltk.download('punkt')
nltk.download('stopwords')
import sys


# Prepare your data# Prepare your data
questions = ["What is your name?", "How are you?", "What is the weather today?", "What is the weather tomorrow in Maastricht?","How is the weather in Berlin? ","My mother is in New York today. What is the weather?","How is the weather in New York today? "]
answers = ["My name is ChatBot.", "I'm doing well, thank you.", "The weather is rainy.","The weather tomorrow in Maastricht will be sunny","It is rainy","It is stormy in newYork","It is stormy in newYork"]

# Preprocess your data
stop_words = set(stopwords.words('english'))

def preprocess_text(text):
    tokens = nltk.word_tokenize(text.lower())
    tokens = [token for token in tokens if token.isalpha() and token not in stop_words]
    return ' '.join(tokens)

preprocessed_questions = [preprocess_text(question) for question in questions]

# Calculate TF-IDF
vectorizer = TfidfVectorizer()
tfidf_matrix = vectorizer.fit_transform(preprocessed_questions)

user_question = sys.argv[1]

# Retrieve answers
def get_answer(user_question):
    preprocessed_question = preprocess_text(user_question)
    tfidf_vector = vectorizer.transform([preprocessed_question])
    similarities = cosine_similarity(tfidf_vector, tfidf_matrix)
    most_similar_index = similarities.argmax()
    return answers[most_similar_index]



# Test the chatbot
#user_question = "What is the weather tomorrow in Maastricht"
response = get_answer(user_question)
print(response)
