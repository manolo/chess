# What is this project

The code here was produced by ChatGPT, I needed a long conversation asking how to create a vaadin flow project and a chess board.
It was necessary to give some tips, like using the correct version of vaadin components, the layouts, removing gaps between squares, using unicode chars instead of the suggested non functional icons, etc.

The project is not complete since it does not prevents invalid movements, nor keep in account player turn, and does not integrates any chess engine or AI interaction. But the idea might be to continue it in next hackathons.

This is what it does so far:

![chess-demo](https://github.com/manolo/chess/assets/161853/13f2a342-4bcf-4321-8524-d896f3178bc5)


# Project Base for a Vaadin application

This project can be used as a starting point to create your own Vaadin application.
It has the necessary dependencies and files to help you get started.
It requires Java 8 or newer and node.js 10.16 or newer.

To run the project, run `mvn jetty:run` and open [http://localhost:8080](http://localhost:8080) in browser.

To update to the latest available Vaadin release, issue `mvn 
versions:update-properties`

Some useful links:
- [Feature overview](https://vaadin.com/flow)
- [Documentation](https://vaadin.com/docs/flow/Overview.html)
- [Tutorials](https://vaadin.com/tutorials?q=tag:Flow) 
- [Component Java integrations and examples](https://vaadin.com/components)
