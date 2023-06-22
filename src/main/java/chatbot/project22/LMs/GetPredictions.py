import transformers
import numpy as np
import torch
from transformers import T5Tokenizer, T5ForConditionalGeneration
from transformers import AutoModelForQuestionAnswering, AutoTokenizer, AutoModelForCausalLM
from transformers import pipeline
import json
import sys
from transformers import AutoModelForCausalLM, AutoTokenizer

## QA models:

def roberta_pred(question, context):
    model_name = "deepset/roberta-base-squad2"
    model = AutoModelForQuestionAnswering.from_pretrained(model_name)
    tokenizer = AutoTokenizer.from_pretrained(model_name)
    nlp = pipeline('question-answering', model=model, tokenizer=tokenizer)
    result = nlp(question=question, context=context)
    # if score is NOT good enough, ask for more context
    if result['score'] < 0.0:
        result = 'Please provide more context'
    # else return the answer
    else:
        result = result['answer']
    # print(result)
    return result

def bart_pred(question, context):
    model_name = 'valhalla/bart-large-finetuned-squadv1'
    model = AutoModelForQuestionAnswering.from_pretrained(model_name)
    tokenizer = AutoTokenizer.from_pretrained(model_name)
    nlp = pipeline('question-answering', model=model, tokenizer=tokenizer)
    result = nlp(question=question, context=context)
    # if score is NOT good enough, ask for more context
    if result['score'] < 0.0:
        result = 'Please provide more context'
    # else return the answer
    else:
        result = result['answer']
    # print(result)
    return result

## Summarization models:

def bart_large_cnn_sum(text):
    model_name = 'facebook/bart-large-cnn'
    nlp = pipeline('summarization', model=model_name, tokenizer=model_name)
    result = nlp(text)
    # print(result)
    return result[0]['summary_text']

def t5_base_cnn_sum(text):
    model_name = 'flax-community/t5-base-cnn-dm'
    model = T5ForConditionalGeneration.from_pretrained(model_name)
    tokenizer = T5Tokenizer.from_pretrained(model_name)
    nlp = pipeline("summarization", model=model, tokenizer=tokenizer)
    result = nlp(text)
    # print(res)
    return result[0]['summary_text']

## DialoGPT (the top chatbot)

def dialogpt_medium(user_input, chat_history=None):
    tokenizer = AutoTokenizer.from_pretrained("microsoft/DialoGPT-medium")
    model = AutoModelForCausalLM.from_pretrained("microsoft/DialoGPT-medium")
    user_input_ids = tokenizer.encode(user_input + tokenizer.eos_token, return_tensors='pt')
    if chat_history is not None:
        chat_history_ids = tokenizer.encode(chat_history + tokenizer.eos_token, return_tensors='pt')
        input_ids = torch.cat([chat_history_ids, user_input_ids], dim=-1)
    else:
        input_ids = user_input_ids
    output = model.generate(input_ids, max_length=1000, pad_token_id=tokenizer.eos_token_id)
    response = tokenizer.decode(output[:, input_ids.shape[-1]:][0], skip_special_tokens=True)
    return response

if __name__ == '__main__':

    # print("Received arguments:", sys.argv)

    data = json.loads(sys.argv[1])
    method = data['method']
    params = data['params']

    if method == 'roberta_pred':
        question = params['question']
        context = params['context']
        result = roberta_pred(question, context)
        print(result)
    elif method == 'bart_pred':
        question = params['question']
        context = params['context']
        result = bart_pred(question, context)
        print(result)
    elif method == 'bart_large_cnn_sum':
        text = params['text']
        result = bart_large_cnn_sum(text)
        print(result)
    elif method == 't5_base_cnn_sum':
        text = params['text']
        result = t5_base_cnn_sum(text)
        print(result)
    elif method == 'dialogpt_medium':
        user_input = params['user_input']
        chat_history = params['chat_history']
        result = dialogpt_medium(user_input, chat_history)
        print(result)
