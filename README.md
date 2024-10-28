# Ad Skipper: In-Video Ad Skipping Chrome Extension

[![GitHub Repo](https://img.shields.io/badge/GitHub-Repo-blue)](https://github.com/nipunapamuditha/In-Video-Ad-Skipping-Chrome-Extension)
[![Chrome Web Store](https://img.shields.io/badge/Chrome%20Web%20Store-Extension-orange)](https://chromewebstore.google.com/detail/skip-in-video-ads/dclnaigapefcgpdkobpcofgifgpdfcnd)

## Problem Statement

While there are numerous Chrome extensions that block targeted advertisements on YouTube, a significant issue remains: **50% of regularly watched videos on YouTube contain in-video sponsored segments** that cannot be skipped using conventional ad blockers. This is where **Ad Skipper** comes into play.

Current solutions, such as SponsorBlock, rely entirely on crowdsourcing to identify the timestamps of sponsored segments. This method is time-consuming and often inefficient. 

## Solution Overview

Ad Skipper leverages the power of Large Language Models (LLMs) to improve the process of identifying in-video ads. The extension operates within the Chrome Web Store and utilizes the following workflow:

1. **Transcript Fetching**: When a new YouTube video loads, the extension retrieves the video's transcript.
2. **API Integration**: It then makes a GET API call to the **Ad Skipper API**, which acts as an intermediary between the LLM (Gemini 1.5 Flash) and the extension.
3. **Timestamp Analysis**: The fetched transcript is sent to Gemini 1.5 Flash to analyze and determine the timestamps of sponsored segments.
4. **Automatic Skipping**: The extension automatically skips the identified sponsored segments without any manual user intervention.

### LLM Performance

I have tested both Gemini 1.5 Flash and OpenAI's GPT-4o. Gemini 1.5 Flash proved to be faster and more accurate, likely due to its training data that includes YouTube content.

## Challenges and Future Improvements

- **YouTube API Restrictions**: The initial plan was to transfer the YouTube video ID to the API and extract the transcript using the YouTube Data API. However, YouTube restricts API access from data center IP ranges, which caused failures when using EC2 instances. The extension now directly checks for the "Show Transcript" button to retrieve transcripts.
  
- **Timestamp Accuracy**: The accuracy of the timestamps provided by the LLM is challenged by YouTube's auto-generated transcripts, which do not provide sentence-ending timestamps. This results in a slight margin of error, typically within two seconds. Future improvements will focus on calculating the exact start and end times of segments by retrieving the duration of the final sentence.

- **Crowdsourcing for Accuracy**: The optimal approach would involve leveraging a crowdsourced platform while using the Gemini API as a fallback when timestamps aren't available in the public database. This improvement is planned for the next version of Ad Skipper, pending Chrome Store approval.

## Conclusion

Ad Skipper aims to revolutionize the way users experience sponsored segments on YouTube, making video consumption more enjoyable. Your feedback and contributions are welcome!

## Getting Started

To install the Ad Skipper extension, visit the [Chrome Web Store](https://chromewebstore.google.com/detail/skip-in-video-ads/dclnaigapefcgpdkobpcofgifgpdfcnd).

For source code and further development details, check out the [GitHub repository](https://github.com/nipunapamuditha/In-Video-Ad-Skipping-Chrome-Extension).

## License

This project is licensed under the MIT License. See the LICENSE file for more information.

