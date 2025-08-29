<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>3조 쇼핑몰 ERP 프로젝트 (쉬운 설명 Ver.)</title>
    <style>
        /* CSS 스타일 코드는 이전과 동일합니다. (생략) */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Malgun Gothic', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: #333;
            overflow: hidden;
        }

        .presentation-container {
            width: 100vw;
            height: 100vh;
            position: relative;
        }

        .slide {
            width: 100%;
            height: 100%;
            padding: 40px 60px;
            display: none;
            background: white;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
        }

        .slide.active {
            display: flex;
            flex-direction: column;
        }

        .slide h1 {
            font-size: 3.5em;
            color: #2c3e50;
            margin-bottom: 20px;
            text-align: center;
            font-weight: 700;
            background: linear-gradient(45deg, #667eea, #764ba2);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }
        
        .slide h1 .subtitle {
            display: block;
            font-size: 0.5em;
            color: #5a6c7d;
            margin-top: 10px;
            -webkit-text-fill-color: #5a6c7d;
        }

        .slide h2 {
            font-size: 2.8em;
            color: #34495e;
            margin-bottom: 25px;
            font-weight: 600;
            border-bottom: 3px solid #667eea;
            padding-bottom: 10px;
        }

        .slide h3 {
            font-size: 2.2em;
            color: #5a6c7d;
            margin-bottom: 20px;
            font-weight: 600;
        }

        .slide h4 {
            font-size: 1.8em;
            color: #667eea;
            margin-bottom: 15px;
            font-weight: 600;
        }

        .slide p, .slide li {
            font-size: 1.3em;
            line-height: 1.8;
            margin-bottom: 12px;
            color: #2c3e50;
        }

        .slide ul {
            margin-left: 30px;
            margin-bottom: 20px;
        }

        .slide li {
            margin-bottom: 10px;
        }

        .code-block {
            background: #f8f9fa;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            padding: 20px;
            font-family: 'Consolas', monospace;
            font-size: 1.1em;
            line-height: 1.6;
            margin: 15px 0;
            overflow-x: auto;
        }

        .structure-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            grid-template-rows: auto auto auto;
            gap: 20px;
            height: 70%;
            margin: 20px 0;
        }

        .structure-section {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
            padding: 20px;
            border-radius: 15px;
            border-left: 5px solid #667eea;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .structure-section h3 {
            color: #667eea;
            margin-bottom: 15px;
            font-size: 1.6em;
            font-weight: 700;
        }

        .structure-section.full-width {
            grid-column: 1 / -1;
        }

        .structure-box {
            background: white;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 15px;
            border: 2px solid #e9ecef;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        }

        .structure-box:last-child {
            margin-bottom: 0;
        }

        .structure-box h4 {
            color: #495057;
            font-size: 1.3em;
            margin-bottom: 10px;
            font-weight: 600;
        }

        .structure-box p {
            font-size: 1.1em;
            line-height: 1.6;
            margin-bottom: 8px;
            color: #495057;
        }

        .structure-box p:last-child {
            margin-bottom: 0;
        }

        .navigation {
            position: fixed;
            bottom: 30px;
            right: 30px;
            z-index: 1000;
        }

        .nav-button {
            background: #667eea;
            color: white;
            border: none;
            padding: 15px 25px;
            margin: 0 5px;
            border-radius: 25px;
            cursor: pointer;
            font-size: 1.1em;
            font-weight: 600;
            transition: all 0.3s ease;
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }

        .nav-button:hover {
            background: #5a67d8;
            transform: translateY(-2px);
            box-shadow: 0 7px 20px rgba(0,0,0,0.3);
        }

        .nav-button:disabled {
            background: #bdc3c7;
            cursor: not-allowed;
            transform: none;
        }

        .slide-counter {
            position: fixed;
            top: 30px;
            right: 30px;
            background: rgba(0,0,0,0.7);
            color: white;
            padding: 10px 20px;
            border-radius: 20px;
            font-size: 1.1em;
            font-weight: 600;
        }

        .team-info {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            border-radius: 15px;
            margin: 20px 0;
            text-align: center;
        }

        .team-member {
            display: inline-block;
            background: rgba(255,255,255,0.2);
            padding: 15px 25px;
            margin: 10px;
            border-radius: 10px;
            font-size: 1.2em;
            font-weight: 600;
        }

        .mvc-diagram {
            display: flex;
            justify-content: space-around;
            align-items: center;
            margin: 30px 0;
            text-align: center;
        }

        .mvc-box {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
            color: #333;
            padding: 25px;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
            flex: 1;
            margin: 0 15px;
            border: 2px solid #667eea;
        }
        
        .mvc-box .icon {
            font-size: 3em;
            margin-bottom: 15px;
        }

        .mvc-box h4 {
            color: #667eea;
            font-size: 1.5em;
            margin-bottom: 10px;
        }

        .mvc-box p {
            font-size: 1.1em;
        }

        .arrow {
            font-size: 3em;
            color: #667eea;
            font-weight: bold;
        }

        .data-flow {
            background: #f8f9fa;
            border-left: 5px solid #667eea;
            padding: 20px;
            margin: 20px 0;
            font-size: 1.2em;
            line-height: 1.8;
        }

        .highlight {
            background: linear-gradient(120deg, #ffeaa7 0%, #fdcb6e 100%);
            padding: 20px;
            border-radius: 10px;
            margin: 20px 0;
            border: 2px solid #e17055;
        }

        .servlet-methods-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin: 20px 0;
        }

        .method-category {
            background: linear-gradient(135deg, #e8f4fd 0%, #d6eaff 100%);
            padding: 20px;
            border-radius: 12px;
            border-left: 5px solid #3498db;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }

        .method-category.full-width {
            grid-column: 1 / -1;
            margin-top: 20px;
        }

        .method-category h4 {
            color: #2c3e50;
            font-size: 1.4em;
            margin-bottom: 15px;
            font-weight: 600;
            text-align: center;
        }

        .method-detail {
            background: white;
            padding: 15px;
            border-radius: 8px;
            border: 2px solid #bdc3c7;
        }

        .method-detail p {
            font-size: 1.2em;
            line-height: 1.6;
            margin-bottom: 8px;
            color: #2c3e50;
        }

        .code-detail {
            background: #2c3e50;
            color: #ecf0f1;
            padding: 20px;
            border-radius: 8px;
            font-family: 'Consolas', monospace;
        }

        .code-detail p {
            font-size: 1.1em;
            line-height: 1.8;
            margin-bottom: 10px;
            color: #ecf0f1;
        }

        .code-detail p:last-child {
            margin-bottom: 0;
        }

        .variable-list {
            background: #e8f4fd;
            padding: 20px;
            border-radius: 10px;
            margin: 15px 0;
            border: 2px solid #3498db;
        }
    </style>
</head>
<body>