# FieldWireCC

I have completed key features for this project:
1,The app uses MVP architecture to decouple codes. To avoid crashing, I take many things into consideration such as Retrofit asynchronous operations ,activity life cycle callbacks, networking errors
2,Self defined scroll listener was made to implement Infinite scrolling , elegant Glide and Retrofit cache configurations were made to achieve precaching
3,SearchView+RecyclerView+File IO for search history implementation
4,Custom photo view was made for full screen image view , swipe ,zoom functionalities

The issue I haven't solved:
I was unable to parse two different type json objects in one json file. Currently, all retrieved images are non album images. With only "gallery search" APIs,  it's likely to fetch album images with multiple requests for one query, but I don't see it as an elegant way. BTW, the example query "hyperloop shuttle" has no results, in any combinations of the given query parameters.

The implementable improvements,but I don't have time for them now given only two days:
1, Search View history filter(now it's just history list view without filter)
2,Animation between thumbnails and full screen mode
3,Settings for different search strategies(Considering back end server provides advanced query parameters )
4,Save image to local storage
5,Of course, GUI will be better with more static resources

____________________________________________________________________________________________________________

01/31/2019, now, the follow up is to implement my own image loading cache library, zoomable custom imageview, search view debounce,
sqlite for search history, also TripActions's code challenge is basicly to add NY Times top story apis, all other features are
the same or even simpler
