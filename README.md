# Welcome to Yet Another Challenge Application!

### To setup your development environment:
1. Clone the repository:
   * `$ git clone https://github.com/TophPT/commit-viewer-challenge.git\`
   * `$ cd commit-viewer-challenge`
2. Either configure Maven or use your IDE build in Maven:
   * `$ mvn clean install`
   * `$ mvn start`
---

Server is now listening on: is now listening on: `http://localhost:8080/api/commit-viewer/`

### User guide:
1. Copy the `/owner/repo` part from a public repository, e.g.: `/TophPT/commit-viewer-challenge`
2. Paste it after our endpoint: `http://localhost:8080/api/commit-viewer/TophPT/commit-viewer-challenge`

### Everything that you should know before you look into the code:
 * I've never started an application from scratch on my own: That was challenging!
   * As you know, I'll love to code in Scala again, but I do not have that much experience on building an application from zero..
   * So I've done this challenge in **Java / Spring**
 * Data is not beeing stored at this moment. Which means each request access GitHubAPI / GitCLI to fetach commits;
 * Error handling is poor, althought it was not left apart completely, but I'm not that happy with it;
 * Git CLI could be improved, but I only thought about it after.., and added a few TODO on how it could be improved;
 * Although pagination was mentioned, I didn't had that much time to work it (here it is how I would try):
   * Let's say our server could handle 500 commits per request.
   * I would request 500, till I get less than 500 or an empty list: this should be possible with both git cli and github api
   * **P.S.**: I believe there's not any way of knowing how many commits a repo has.
 * The application has only one endpoint, which means that we cannot fetch commits from a specific branch, but that would be also usefull.
 * I've tried to figure out a different way of implementing this git cli version, because I do not like running this `git clone ..` for each repo:
   * Since I couldn't, I'm checking the least ammount of data for a `tmpdir` folder;
   * This folder should be removed after the data parsed;

Hope to talk to you soon, enjoy ;)
